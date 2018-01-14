package by.andruhovich.subscription.dao;

import by.andruhovich.subscription.exception.DAOTechnicalException;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public abstract class BaseDAO {
    protected Connection connection;

    public BaseDAO(Connection connection) {
        this.connection = connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void close(Statement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            // TODO log
        }
    }
}
