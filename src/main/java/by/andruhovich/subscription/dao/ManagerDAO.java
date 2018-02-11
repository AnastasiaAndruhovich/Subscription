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

/**
 * @param <T> get any child Entity class
 *           represents abstract methods for realizing it by DAO children
 */
public abstract class ManagerDAO <T extends Entity> extends BaseDAO {
    private static final Logger LOGGER = LogManager.getLogger(ManagerDAO.class);

    /**
     * @param connection java.sql.Connection to initialize super class
     */
    public ManagerDAO(Connection connection) {
        super(connection);
    }

    /**
     * @param entity Entity to be set in database
     * @return The entity id in database
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    public abstract int create(T entity) throws DAOTechnicalException;

    /**
     * @param id Entity id to be deleted from database
     * @return {@code true} if the operation has been completed successfully
     *         {@code false} otherwise
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    public abstract boolean delete(int id) throws DAOTechnicalException;

    /**
     * @param startIndex Entity start index in database
     * @param endIndex Entity end index in database
     * @return Entity List from database
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    public abstract List<T> findAll(int startIndex, int endIndex) throws DAOTechnicalException;

    /**
     * @param  entity Entity to be updated in database
     * @return {@code true} if the operation has been completed successfully
     *         {@code false} otherwise
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    public abstract boolean update(T entity) throws DAOTechnicalException;

    /**
     * @param id Entity id to be found in database
     * @return Entity extends T from database
     * @throws DAOTechnicalException
 *              If there was an error during query execute
     */
    public abstract T findEntityById(int id) throws DAOTechnicalException;

    /**
     * @return Entity extends T count in database
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    public abstract int findEntityCount() throws DAOTechnicalException;

    /**
     * @param mysqlQuery Final {@code String} contains query
     * @return Entity extends T count in database
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
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

    /**
     * @param mysqlQuery Final {@code String} contains query
     * @param id Entity id in database
     * @return Entity extends T count in database
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    protected int findEntityCountById(String mysqlQuery, int id) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for find entity count by id");
        PreparedStatement preparedStatement = null;
        int count = -1;

        try {
            preparedStatement = connection.prepareStatement(mysqlQuery);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                count = resultSet.getInt("count");
            }
            LOGGER.log(Level.INFO, "Request for find entity count bu id - succeed");
            return count;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    /**
     * @param id Entity id in database
     * @param mysqlQuery Final {@code String} contains query
     * @return {@code true} if the operation has been completed successfully
     *         {@code false} otherwise
     * @throws DAOTechnicalException
     */
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
