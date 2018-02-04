package by.andruhovich.subscription.pool;

import by.andruhovich.subscription.exception.ConnectionTechnicalException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;

public class ConnectionFactory {
    private static ConnectionFactory instance;
    private ConnectionPool connectionPool;

    private static final Logger LOGGER = LogManager.getLogger(ConnectionFactory.class);

    private ConnectionFactory() {
        connectionPool = ConnectionPool.getInstance();
    }

    public static ConnectionFactory getInstance() {
        if (instance == null) {
            instance = new ConnectionFactory();
        }
        return instance;
    }

    public Connection getConnection() throws ConnectionTechnicalException {
        Connection connection = connectionPool.getConnection();
        if (connection != null) {
            return connection;
        }
        else {
            throw new ConnectionTechnicalException("Can't get connection with database!");
        }
    }

    public void returnConnection(Connection connection) {
        if (connection != null) {
            connectionPool.returnConnection(connection);
        }
    }
}
