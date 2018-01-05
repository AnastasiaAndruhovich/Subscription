package by.andruhovich.subscription.dao;

import by.andruhovich.subscription.exception.DAOTechnicalException;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public abstract class BaseDAO <T> {
    protected Connection connection;

    public BaseDAO(Connection connection) {
        this.connection = connection;
    }

    public abstract boolean create(T entity);
    public abstract boolean delete(int id);
    public abstract boolean delete(T entity);
    public abstract T findEntityById(int id) throws DAOTechnicalException;
    public abstract List<T> findAll() throws DAOTechnicalException;
    public abstract boolean update(T entity);

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
