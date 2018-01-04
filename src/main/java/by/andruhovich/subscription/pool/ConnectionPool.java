package by.andruhovich.subscription.pool;

import by.andruhovich.subscription.exception.InterruptedTechnicalException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectionPool {
    private static ConnectionPool instance ;
    private static AtomicBoolean instanceCreated = new AtomicBoolean();
    private static Lock instanceLock = new ReentrantLock();
    private static Lock getConnectionLock = new ReentrantLock();
    private Queue<Connection> connections;

    //TODO private static final Logger LOGGER = LogManager.getLogger(ConnectionPool.class);

    private ConnectionPool() {
        DatabaseManager databaseManager = DatabaseManager.getInstance();
        final int POOL_SIZE = Integer.valueOf(databaseManager.getProperty("pool_size"));
        final String URL = databaseManager.getProperty("url");
        final String DRIVER_NAME = databaseManager.getProperty("driver");
        final String USER = databaseManager.getProperty("user");
        final String PASSWORD = databaseManager.getProperty("password");

        connections = new LinkedList<>();
        try {
            Class.forName(DRIVER_NAME);
            for (int i = 0; i < POOL_SIZE; i++) {
                Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                connections.add(connection);
            }
        } catch (SQLException ex) {
            // TODO log
            throw new RuntimeException(ex.getMessage());
        } catch(ClassNotFoundException ex) {
            // TODO LOGGER.log(Level.ERROR, "Driver" + DRIVER_NAME +  "not found.");
            throw new RuntimeException(ex.getMessage());
        }
    }

    public static ConnectionPool getInstance() {
        if (!instanceCreated.get()) {
            try {
                instanceLock.lock();
                if (instance != null && !instanceCreated.get()) {
                    instance = new ConnectionPool();
                    instanceCreated.set(true);
                }
            }  finally {
                instanceLock.unlock();
            }
        }
        return instance;
    }

    public Connection getConnection(long maxSeconds) throws InterruptedTechnicalException {
        Connection connection;
        try {
            if (getConnectionLock.tryLock(maxSeconds, TimeUnit.SECONDS)) {
                connection = connections.poll();
                return connection;
            }
        } catch (InterruptedException e) {
           //TODO LOGGER.log(Level.ERROR, e.getMessage());
            throw new InterruptedTechnicalException(e.getMessage());
        } finally {
            getConnectionLock.unlock();
        }
        return null;
    }

    public void returnConnection(Connection connection) {
        connections.add(connection);
    }
}
