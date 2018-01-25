package by.andruhovich.subscription.pool;


import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    private Queue<Connection> connections = new LinkedList<>();;
    private static int waitingTime;

    private static final Logger LOGGER = LogManager.getLogger(ConnectionPool.class);

    private ConnectionPool() {
        DatabaseManager databaseManager = DatabaseManager.getInstance();
        final int POOL_SIZE = Integer.valueOf(databaseManager.getProperty("pool_size"));
        final String URL = databaseManager.getProperty("url");
        final String DRIVER_NAME = databaseManager.getProperty("driver");
        final String USER = databaseManager.getProperty("user");
        final String PASSWORD = databaseManager.getProperty("password");
        waitingTime = Integer.valueOf(databaseManager.getProperty("waiting_time"));

        try {
            Class.forName(DRIVER_NAME);
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.FATAL, "Driver" + DRIVER_NAME +  "not found.");
            throw new RuntimeException("Driver" + DRIVER_NAME +  "not found.", e);
        }

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
    }

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

    public void returnConnection(Connection connection) {
        connections.add(connection);
    }

    public void closeConnectionPool() {
        for (int i = 0; i < connections.size(); i++) {
            try {
                getConnection().close();
            } catch (SQLException e) {
                LOGGER.log(Level.ERROR, "Closing connection pool error");
            }
        }
    }
}
