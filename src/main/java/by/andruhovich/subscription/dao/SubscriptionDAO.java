package by.andruhovich.subscription.dao;

import by.andruhovich.subscription.entity.Subscription;
import by.andruhovich.subscription.exception.DAOTechnicalException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class SubscriptionDAO extends SubscriptionManagerDAO {
    private static final String INSERT_SUBSCRIPTION= "INSERT INTO subscriptions(user_id, publication_id, start_data, " +
            "end_data, subscription_is_active) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_LAST_INSERT_ID = "SELECT LAST_INSERT_ID()";
    private static final String DELETE_SUBSCRIPTION_BY_ID = "DELETE FROM subscriptions WHERE subscription_id = ?";
    private static final String SELECT_SUBSCRIPTION_BY_ID = "SELECT * FROM subscriptions WHERE subscription_id = ?";
    private static final String SELECT_ALL_SUBSCRIPTIONS = "SELECT * FROM subscriptions";
    private static final String UPDATE_SUBSCRIPTION = "UPDATE subscriptions SET user_id = ?, publication_id = ?, " +
            "start_data = ?, end_data = ?, subscription_is_active = ? WHERE subscription_id = ?";

    @Override
    public int create(Subscription entity) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(INSERT_SUBSCRIPTION);
            preparedStatement = fillOutStatementBySubscription(preparedStatement, entity);
            preparedStatement.executeQuery();
            preparedStatement = connection.prepareStatement(SELECT_LAST_INSERT_ID);
            ResultSet resultSet = preparedStatement.executeQuery();
            int id = resultSet.getInt("subscription_id");
            return id;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public boolean delete(Subscription entity) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(DELETE_SUBSCRIPTION_BY_ID);
            preparedStatement.setInt(1, entity.getSubscriptionId());
            preparedStatement.executeQuery();
            return true;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public Subscription findEntityById(int id) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;
        List<Subscription> subscriptions;

        try {
            preparedStatement = connection.prepareStatement(SELECT_SUBSCRIPTION_BY_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            subscriptions = createSubscriptionList(resultSet);
            return subscriptions.get(0);
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public List<Subscription> findAll() throws DAOTechnicalException {
        List<Subscription> subscriptions;
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SELECT_ALL_SUBSCRIPTIONS);
            ResultSet resultSet = preparedStatement.executeQuery();
            subscriptions = createSubscriptionList(resultSet);
            return subscriptions;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public boolean update(Subscription entity) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(UPDATE_SUBSCRIPTION);
            preparedStatement = fillOutStatementBySubscription(preparedStatement, entity);
            preparedStatement.executeQuery();
            return true;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        } finally {
            close(preparedStatement);
        }
    }

    private PreparedStatement fillOutStatementBySubscription(PreparedStatement preparedStatement, Subscription subscription)
            throws DAOTechnicalException {
        TypeConverter typeConverter = new TypeConverter();

        java.sql.Date startDate = new java.sql.Date(subscription.getStartDate().getTime());
        java.sql.Date endDate = new java.sql.Date(subscription.getEndDate().getTime());
        String subscriptionIsActive = typeConverter.convertBooleanToString(subscription.getSubscriptionIsActive());

        try {
            preparedStatement.setInt(1, subscription.getUserId());
            preparedStatement.setInt(2, subscription.getPublicationId());
            preparedStatement.setDate(3, startDate);
            preparedStatement.setDate(4, endDate);
            preparedStatement.setString(5, subscriptionIsActive);
            return preparedStatement;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        }
    }

    private List<Subscription> createSubscriptionList(ResultSet resultSet) throws DAOTechnicalException {
        List<Subscription> subscriptions = new LinkedList<>();
        Subscription subscription;
        TypeConverter typeConverter = new TypeConverter();

        try {
            while (resultSet.next()) {
                int subscriptionId = resultSet.getInt("subscription_id");
                int user_id = resultSet.getInt("user_id");
                int publication_id = resultSet.getInt("publication_id");
                java.util.Date startDate = new java.util.Date(resultSet.getDate("startDate").getTime());
                java.util.Date endDate = new java.util.Date(resultSet.getDate("endDate").getTime());
                boolean subscriptionIsActive = typeConverter.convertStringToBoolean(resultSet.getString("subscription_is_active"));
                subscription = new Subscription(subscriptionId, user_id, publication_id, startDate, endDate, subscriptionIsActive);
                subscriptions.add(subscription);
            }
            return subscriptions;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        }
    }
}
