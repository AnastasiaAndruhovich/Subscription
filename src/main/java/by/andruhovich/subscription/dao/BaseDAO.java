package by.andruhovich.subscription.dao;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Represents abstract base DAO class
 */
public abstract class BaseDAO {
    protected Connection connection;

    private static final Logger LOGGER = LogManager.getLogger(BaseDAO.class);

    /**
     * @param connection connection represents java.sql.Connection
     */
    BaseDAO(Connection connection) {
        this.connection = connection;
    }

    Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    /**
     * @param statement statement with query for closing
     */
    public void close(Statement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.ERROR, "Error close statement");
        }
    }
}
