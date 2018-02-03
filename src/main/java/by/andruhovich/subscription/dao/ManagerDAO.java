package by.andruhovich.subscription.dao;

import by.andruhovich.subscription.entity.Entity;
import by.andruhovich.subscription.exception.DAOTechnicalException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    private static final Logger LOGGER = LogManager.getLogger(ManagerDAO.class);

    protected int findEntityCount(String mysqlQuery) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for find entity count");
        PreparedStatement preparedStatement = null;
        int count = -1;

        try {
            preparedStatement = connection.prepareStatement(mysqlQuery);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                count = resultSet.getInt("count");
            }
            LOGGER.log(Level.INFO, "Request for find entity count - succeed");
            return count;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    protected boolean delete(int id, String mysqlQuery) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for delete entity");
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(mysqlQuery);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            LOGGER.log(Level.INFO, "Request for delete entity - succeed");
            return true;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }
}
