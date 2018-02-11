package by.andruhovich.subscription.pool;


import by.andruhovich.subscription.exception.MissingResourceTechnicalException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Contains database connection queue and provides methods to work with it
 */
public class ConnectionPool {
    private static ConnectionPool instance ;
    private static AtomicBoolean instanceCreated = new AtomicBoolean();
    private static Lock instanceLock = new ReentrantLock();
    private static Lock getConnectionLock = new ReentrantLock();
    private BlockingQueue<Connection> connections;
    private static int waitingTime;

    private static final Logger LOGGER = LogManager.getLogger(ConnectionPool.class);

    /**
     * Initialize connection pool variables
     */
    private ConnectionPool() {
        DatabaseManager databaseManager = DatabaseManager.getInstance();
        try {
            final int POOL_SIZE = Integer.valueOf(databaseManager.getProperty("pool_size"));
            final String URL = databaseManager.getProperty("url");
            final String DRIVER_NAME = databaseManager.getProperty("driver");
            final String USER = databaseManager.getProperty("user");
            final String PASSWORD = databaseManager.getProperty("password");
            waitingTime = Integer.valueOf(databaseManager.getProperty("waiting_time"));

            Class.forName(DRIVER_NAME);

            connections = new ArrayBlockingQueue<>(POOL_SIZE);
            for (int i = 0; i < POOL_SIZE; i++) {
                try {
                    Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                    connections.add(connection);
                } catch (SQLException e) {
                    LOGGER.log(Level.ERROR, "Getting connection error");
                }
            }

            if (connections.isEmpty()) {
                LOGGER.log(Level.FATAL, "It was not succeeded to create database connections. Connection pool is empty");
                throw new RuntimeException("It was not succeeded to create database connections. Connection pool is empty");
            }

        } catch (MissingResourceTechnicalException e) {
            LOGGER.log(Level.FATAL, "It was not succeeded to create database connections. " + e.getMessage());
            throw new RuntimeException("It was not succeeded to create database connections. " + e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.FATAL, "Driver is not found");
            throw new RuntimeException("Driver is not found", e);
        }
    }

    /**
     * @return ConnectionPool reference
     */
    public static ConnectionPool getInstance() {
        if (!instanceCreated.get()) {
            try {
                instanceLock.lock();
                if (instance == null && !instanceCreated.get()) {
                    instance = new ConnectionPool();
                    instanceCreated.set(true);
                }
            }  finally {
                instanceLock.unlock();
            }
        }
        return instance;
    }

    /**
     * @return java.sql.Connection or null in exception case
     */
    public Connection getConnection() {
        Connection connection;
        try {
            if (getConnectionLock.tryLock(waitingTime, TimeUnit.SECONDS)) {
                connection = connections.poll();
                return connection;
            }
        } catch (InterruptedException e) {
           LOGGER.log(Level.ERROR, "Getting connection error");
        } finally {
            getConnectionLock.unlock();
        }
        return null;
    }

    /**
     * @param connection java.sql.Connection for return in connection pool
     */
    public void returnConnection(Connection connection) {
        connections.add(connection);
    }

    public void closeConnectionPool() {
        for (int i = 0; i < connections.size(); i++) {
            try {
                connections.take().close();
            } catch (SQLException | InterruptedException e) {
                LOGGER.log(Level.ERROR, "Error closing connection");
            }
        }
        deregisterDriver();
    }

    /**
     * Deregister database driver. It should be call before closing application.
     */
    private void deregisterDriver() {
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
                LOGGER.log(Level.INFO, String.format("Deregistering jdbc driver: %s", driver));
            } catch (SQLException e) {
                LOGGER.log(Level.ERROR, String.format("Error deregistering driver %s", driver), e);
            }
        }
    }
}
