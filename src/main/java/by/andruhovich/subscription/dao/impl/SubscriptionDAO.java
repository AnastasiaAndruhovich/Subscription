package by.andruhovich.subscription.dao.impl;

import by.andruhovich.subscription.dao.SubscriptionManagerDAO;
import by.andruhovich.subscription.entity.Subscription;
import by.andruhovich.subscription.entity.User;
import by.andruhovich.subscription.exception.DAOTechnicalException;
import by.andruhovich.subscription.mapper.SubscriptionMapper;
import by.andruhovich.subscription.mapper.UserMapper;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SubscriptionDAO extends SubscriptionManagerDAO {
    private static final String INSERT_SUBSCRIPTION= "INSERT INTO subscriptions(user_id, publication_id, start_date, " +
            "end_date, subscription_is_active) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_LAST_INSERT_ID = "SELECT subscription_id FROM subscriptions ORDER BY " +
            "subscription_id DESC LIMIT 1";
    private static final String DELETE_SUBSCRIPTION_BY_ID = "DELETE FROM subscriptions WHERE subscription_id = ?";
    private static final String SELECT_COUNT = "SELECT COUNT(subscription_id) AS count FROM subscriptions";
    private static final String SELECT_SUBSCRIPTION_BY_ID = "SELECT subscription_id, start_date, end_date, " +
            "subscription_is_active FROM subscriptions WHERE subscription_id = ?";
    private static final String SELECT_ALL_SUBSCRIPTIONS = "SELECT subscription_id, start_date, end_date, " +
            "subscription_is_active FROM subscriptions ORDER BY subscription_id DESC LIMIT ?, ?";
    private static final String UPDATE_SUBSCRIPTION = "UPDATE subscriptions SET user_id = ?, publication_id = ?, " +
            "start_date = ?, end_date = ?, subscription_is_active = ? WHERE subscription_id = ?";

    private static final String SELECT_USER_BY_SUBSCRIPTION_ID = "SELECT u.user_id, u.lastname, u.firstname, " +
            "u.birthdate, u.address, u.city, u.postal_index, u.login, u.password FROM subscriptions JOIN users u USING (user_id) " +
            "WHERE subscription_id = ?";
    private static final String SELECT_SUBSCRIPTIONS_BY_USER_ID = "SELECT subscription_id, start_date, end_date, " +
            "subscription_is_active FROM subscriptions WHERE user_id = ? ORDER BY user_id DESC LIMIT ?, ?";

    private static final String SELECT_COUNT_BY_USER_ID = "SELECT COUNT(subscription_id) AS count FROM subscriptions WHERE user_id = ?";

    private static final Logger LOGGER = LogManager.getLogger(SubscriptionDAO.class);

    public SubscriptionDAO(Connection connection) {
        super(connection);
    }

    @Override
    public int create(Subscription entity) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for create subscription");
        PreparedStatement preparedStatement = null;
        PreparedStatement statement = null;
        SubscriptionMapper mapper = new SubscriptionMapper();
        int id = -1;

        try {
            preparedStatement = connection.prepareStatement(INSERT_SUBSCRIPTION);
            preparedStatement = mapper.mapEntityToPreparedStatement(preparedStatement, entity);
            preparedStatement.executeUpdate();
            statement = connection.prepareStatement(SELECT_LAST_INSERT_ID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                id = resultSet.getInt("subscription_id");
            }
            LOGGER.log(Level.INFO, "Request for create subscription - succeed");
            return id;
        } catch (MySQLIntegrityConstraintViolationException e) {
            LOGGER.log(Level.INFO, "Subscription is already exist");
            return id;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
            close(statement);
        }
    }

    @Override
    public boolean delete(int id) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for delete subscription");
        return delete(id, DELETE_SUBSCRIPTION_BY_ID);
    }

    @Override
    public Subscription findEntityById(int id) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for find entity by id");
        PreparedStatement preparedStatement = null;
        List<Subscription> subscriptions;

        try {
            preparedStatement = connection.prepareStatement(SELECT_SUBSCRIPTION_BY_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            SubscriptionMapper mapper = new SubscriptionMapper();
            subscriptions = mapper.mapResultSetToEntity(resultSet);
            LOGGER.log(Level.INFO, "Request for find entity by id - succeed");
            if (subscriptions.isEmpty()) {
                return null;
            }
            return subscriptions.get(0);
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public List<Subscription> findAll(int startIndex, int endIndex) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for find all");
        List<Subscription> subscriptions;
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SELECT_ALL_SUBSCRIPTIONS);
            preparedStatement.setInt(1, startIndex);
            preparedStatement.setInt(2, endIndex);
            ResultSet resultSet = preparedStatement.executeQuery();
            SubscriptionMapper mapper = new SubscriptionMapper();
            subscriptions = mapper.mapResultSetToEntity(resultSet);
            LOGGER.log(Level.INFO, "Request for find all - succeed");
            return subscriptions;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public boolean update(Subscription entity) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for update subscription");
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(UPDATE_SUBSCRIPTION);
            SubscriptionMapper mapper = new SubscriptionMapper();
            preparedStatement = mapper.mapEntityToPreparedStatement(preparedStatement, entity);
            preparedStatement.setInt(6, entity.getSubscriptionId());
            preparedStatement.executeUpdate();
            LOGGER.log(Level.INFO, "Request for update subscription - succeed");
            return true;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public User findUserBySubscriptionId(int id) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for find user by subscription id");
        PreparedStatement preparedStatement = null;
        List<User> users;

        try {
            preparedStatement = connection.prepareStatement(SELECT_USER_BY_SUBSCRIPTION_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            UserMapper userMapper = new UserMapper();
            users = userMapper.mapResultSetToEntity(resultSet);
            LOGGER.log(Level.INFO, "Request for find user by subscription id - succeed");
            if (!users.isEmpty()) {
                return users.get(0);
            }
            return null;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public List<Subscription> findSubscriptionsByUserId(int id, int startIndex, int endIndex) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for find subscriptions by user id");
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SELECT_SUBSCRIPTIONS_BY_USER_ID);
            preparedStatement.setInt(1, id);
            preparedStatement.setInt(2, startIndex);
            preparedStatement.setInt(3, endIndex);
            ResultSet resultSet = preparedStatement.executeQuery();
            LOGGER.log(Level.INFO, "Request for find subscriptions by user id - succeed");
            SubscriptionMapper subscriptionMapper = new SubscriptionMapper();
            return subscriptionMapper.mapResultSetToEntity(resultSet);
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public int findSubscriptionCountByUserId(int userId) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for find subscription count by user id");
        return findEntityCountById(SELECT_COUNT_BY_USER_ID, userId);
    }

    public int findEntityCount() throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for get count");
        return findEntityCount(SELECT_COUNT);
    }
}
