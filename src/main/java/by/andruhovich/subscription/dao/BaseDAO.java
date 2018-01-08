package by.andruhovich.subscription.dao;

import by.andruhovich.subscription.exception.DAOTechnicalException;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public abstract class BaseDAO <T> {
    protected Connection connection;

    public abstract int create(T entity) throws DAOTechnicalException;
    public abstract boolean delete(T entity) throws DAOTechnicalException;
    public abstract T findEntityById(int id) throws DAOTechnicalException;
    public abstract List<T> findAll() throws DAOTechnicalException;
    public abstract boolean update(T entity) throws DAOTechnicalException;

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
