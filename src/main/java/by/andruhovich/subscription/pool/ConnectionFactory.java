package by.andruhovich.subscription.pool;

import by.andruhovich.subscription.exception.ConnectionTechnicalException;

import java.sql.Connection;

/**
 * Provides methods to work with database connections
 */
public class ConnectionFactory {
    private static ConnectionFactory instance;
    private ConnectionPool connectionPool;

    /**
     * Initialize connection pool reference
     */
    private ConnectionFactory() {
        connectionPool = ConnectionPool.getInstance();
    }

    /**
     * @return Connection pool reference
     */
    public static ConnectionFactory getInstance() {
        if (instance == null) {
            instance = new ConnectionFactory();
        }
        return instance;
    }

    /**
     * @return java.sql.Connection
     * @throws ConnectionTechnicalException
     *          If there was an error during getting connection
     */
    public Connection getConnection() throws ConnectionTechnicalException {
        Connection connection = connectionPool.getConnection();
        if (connection != null) {
            return connection;
        }
        else {
            throw new ConnectionTechnicalException("Can't get connection with database!");
        }
    }

    /**
     * @param connection java.sql.Connection for return in connection pool
     */
    public void returnConnection(Connection connection) {
        if (connection != null) {
            connectionPool.returnConnection(connection);
        }
    }
}
