package by.andruhovich.subscription.dao;

import by.andruhovich.subscription.entity.Entity;
import by.andruhovich.subscription.exception.DAOTechnicalException;

import java.sql.Connection;
import java.util.List;

public abstract class ManagerDAO <T extends Entity> extends BaseDAO {
    public ManagerDAO(Connection connection) {
        super(connection);
    }

    public abstract int create(T entity) throws DAOTechnicalException;
    public abstract boolean delete(T entity) throws DAOTechnicalException;
    public abstract List<T> findAll() throws DAOTechnicalException;
    public abstract boolean update(T entity) throws DAOTechnicalException;
}
