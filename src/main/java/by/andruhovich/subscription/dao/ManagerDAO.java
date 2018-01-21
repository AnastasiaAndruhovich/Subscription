package by.andruhovich.subscription.dao;

import by.andruhovich.subscription.entity.Entity;
import by.andruhovich.subscription.exception.DAOTechnicalException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public abstract class ManagerDAO <T extends Entity> extends BaseDAO {
    public ManagerDAO(Connection connection) {
        super(connection);
    }

    public abstract int create(T entity) throws DAOTechnicalException;
    public abstract boolean delete(int id) throws DAOTechnicalException;
    public abstract List<T> findAll(int startIndex, int endIndex) throws DAOTechnicalException;
    public abstract boolean update(T entity) throws DAOTechnicalException;

    public boolean delete(int id, String mysqlQuery) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(mysqlQuery);
            preparedStatement.setInt(1, id);
            preparedStatement.executeQuery();
            return true;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }
}
