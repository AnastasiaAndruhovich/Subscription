package by.andruhovich.subscription.dao.impl;

import by.andruhovich.subscription.dao.SubscriptionManagerDAO;
import by.andruhovich.subscription.entity.Payment;
import by.andruhovich.subscription.entity.Publication;
import by.andruhovich.subscription.entity.Subscription;
import by.andruhovich.subscription.entity.User;
import by.andruhovich.subscription.exception.DAOTechnicalException;
import by.andruhovich.subscription.mapper.PublicationMapper;
import by.andruhovich.subscription.mapper.SubscriptionMapper;
import by.andruhovich.subscription.mapper.UserMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SubscriptionDAO extends SubscriptionManagerDAO {
    private static final String INSERT_SUBSCRIPTION= "INSERT INTO subscriptions(user_id, publication_id, start_date, " +
            "end_date, subscription_is_active) VALUES (?, ?, ?, ?, ?)";
    private static final String DELETE_SUBSCRIPTION_BY_ID = "DELETE FROM subscriptions WHERE subscription_id = ?";
    private static final String SELECT_SUBSCRIPTION_BY_ID = "SELECT subscription_id, start_date, end_date, " +
            "subscription_is_active FROM subscriptions WHERE subscription_id = ?";
    private static final String SELECT_ALL_SUBSCRIPTIONS = "SELECT subscription_id, start_date, end_date, " +
            "subscription_is_active FROM subscriptions LIMIT ?, ?";
    private static final String UPDATE_SUBSCRIPTION = "UPDATE subscriptions SET user_id = ?, publication_id = ?, " +
            "start_date = ?, end_date = ?, subscription_is_active = ? WHERE subscription_id = ?";

    private static final String SELECT_USER_BY_SUBSCRIPTION_ID = "SELECT u.user_id, u.lastname, u.firstname, " +
            "u.birthdate, u.address, u.city, u.postal_index FROM subscriptions JOIN users u USING (user_id) " +
            "WHERE subscription_id = ?";
    private static final String SELECT_PUBLICATION_BY_SUBSCRIPTION_ID = "SELECT p.publication_id, p.name, p.description, " +
            "p.price, p.picture_name, p.picture FROM subscriptions JOIN publications p USING (publication_id) " +
            "WHERE subscription_id = ?";
    private static final String SELECT_SUBSCRIPTIONS_BY_USER_ID = "SELECT subscription_id, start_date, end_date, " +
            "subscription_is_active FROM subscriptions WHERE user_id = ?";

    public SubscriptionDAO(Connection connection) {
        super(connection);
    }

    @Override
    public int create(Subscription entity) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;
        SubscriptionMapper mapper = new SubscriptionMapper();
        int id = -1;

        try {
            preparedStatement = connection.prepareStatement(INSERT_SUBSCRIPTION);
            preparedStatement = mapper.mapEntityToPreparedStatement(preparedStatement, entity);
            preparedStatement.executeQuery();
            preparedStatement = connection.prepareStatement(SELECT_LAST_INSERT_ID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                id = resultSet.getInt("subscription_id");
            }
            return id;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public boolean delete(int id) throws DAOTechnicalException {
        return delete(id, DELETE_SUBSCRIPTION_BY_ID);
    }

    @Override
    public Subscription findEntityById(int id) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;
        List<Subscription> subscriptions;

        try {
            preparedStatement = connection.prepareStatement(SELECT_SUBSCRIPTION_BY_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            SubscriptionMapper mapper = new SubscriptionMapper();
            subscriptions = mapper.mapResultSetToEntity(resultSet);
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
        List<Subscription> subscriptions;
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SELECT_ALL_SUBSCRIPTIONS);
            preparedStatement.setInt(1, startIndex);
            preparedStatement.setInt(2, endIndex);
            ResultSet resultSet = preparedStatement.executeQuery();
            SubscriptionMapper mapper = new SubscriptionMapper();
            subscriptions = mapper.mapResultSetToEntity(resultSet);
            return subscriptions;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public boolean update(Subscription entity) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(UPDATE_SUBSCRIPTION);
            SubscriptionMapper mapper = new SubscriptionMapper();
            preparedStatement = mapper.mapEntityToPreparedStatement(preparedStatement, entity);
            preparedStatement.executeQuery();
            return true;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public User findUserBySubscriptionId(int id) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;
        List<User> users;

        try {
            preparedStatement = connection.prepareStatement(SELECT_USER_BY_SUBSCRIPTION_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            UserMapper userMapper = new UserMapper();
            users = userMapper.mapResultSetToEntity(resultSet);
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
    public Publication findPublicationBySubscriptionId(int id) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;
        List<Publication> publications;

        try {
            preparedStatement = connection.prepareStatement(SELECT_PUBLICATION_BY_SUBSCRIPTION_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            PublicationMapper publicationMapper = new PublicationMapper();
            publications = publicationMapper.mapResultSetToEntity(resultSet);
            if (!publications.isEmpty()) {
                return publications.get(0);
            }
            return null;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public List<Payment> findPaymentsBySubscriptionId(int id) throws DAOTechnicalException {
        PaymentDAO paymentDAO = new PaymentDAO(connection);
        return paymentDAO.findPaymentsBySubscriptionId(id);
    }

    @Override
    public List<Subscription> findSubscriptionsByUserId(int id) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SELECT_SUBSCRIPTIONS_BY_USER_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            SubscriptionMapper subscriptionMapper = new SubscriptionMapper();
            return subscriptionMapper.mapResultSetToEntity(resultSet);
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }
}
