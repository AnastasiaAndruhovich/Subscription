package by.andruhovich.subscription.pool;

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

    //private static final Logger LOGGER = LogManager.getLogger(ConnectionPool.class);

    private ConnectionPool() {
        final int POOL_SIZE = Integer.valueOf(DatabaseManager.getProperty("pool_size"));
        final String URL = DatabaseManager.getProperty("url");
        final String DRIVER_NAME = DatabaseManager.getProperty("driver");
        final String USER = DatabaseManager.getProperty("user");
        final String PASSWORD = DatabaseManager.getProperty("password");

        connections = new LinkedList<>();
        try {
            Class.forName(DRIVER_NAME);
            for (int i = 0; i < POOL_SIZE; i++) {
                Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                connections.add(connection);
            }
        } catch (SQLException ex) {
            // ???
          ryruntime exc
        } catch(ClassNotFoundException ex) {
            // ???
            //LOGGER.log(Level.ERROR, "Driver" + DRIVER_NAME +  "not found.");
            System.out.println("Driver" + DRIVER_NAME +  "not found.");
        }
    }

    public static ConnectionPool getInstance() {
        if (!instanceCreated.get()) {
            try {
                instanceLock.lock();
                if (instanceCreated != null && !instanceCreated.get()) {//null - что проверять?
                    instance = new ConnectionPool();
                    instanceCreated.set(true);
                }
            }  finally {
                instanceLock.unlock();
            }
        }
        return instance;
    }

    public Connection getConnection(long maxSeconds) {
        Connection connection;
        try {
            if (getConnectionLock.tryLock(maxSeconds, TimeUnit.SECONDS)) {
                connection = connections.poll();
                return connection;
            }
        } catch (InterruptedException e) {
           //LOGGER.log(Level.ERROR, e.getMessage());
        } finally {
            getConnectionLock.unlock();
        }
        return null;
    }

    public void returnConnection(Connection connection) {
        connections.add(connection);
    }
}
