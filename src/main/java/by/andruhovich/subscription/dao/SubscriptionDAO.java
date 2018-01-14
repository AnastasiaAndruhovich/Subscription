package by.andruhovich.subscription.dao;

import by.andruhovich.subscription.entity.Subscription;
import by.andruhovich.subscription.exception.DAOTechnicalException;
import by.andruhovich.subscription.mapper.SubscriptionMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SubscriptionDAO extends SubscriptionManagerDAO {
    private static final String INSERT_SUBSCRIPTION= "INSERT INTO subscriptions(user_id, publication_id, start_date, " +
            "end_date, subscription_is_active) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_LAST_INSERT_ID = "SELECT LAST_INSERT_ID()";
    private static final String DELETE_SUBSCRIPTION_BY_ID = "DELETE FROM subscriptions WHERE subscription_id = ?";
    private static final String SELECT_SUBSCRIPTION_BY_ID = "SELECT subscription_id, start_date, end_date, " +
            "subscription_is_active FROM subscriptions WHERE subscription_id = ?";
    private static final String SELECT_ALL_SUBSCRIPTIONS = "SELECT subscription_id, start_date, end_date, " +
            "subscription_is_active FROM subscriptions";
    private static final String UPDATE_SUBSCRIPTION = "UPDATE subscriptions SET user_id = ?, publication_id = ?, " +
            "start_date = ?, end_date = ?, subscription_is_active = ? WHERE subscription_id = ?";

    public SubscriptionDAO(Connection connection) {
        super(connection);
    }

    @Override
    public int create(Subscription entity) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;
        SubscriptionMapper mapper = new SubscriptionMapper();

        try {
            preparedStatement = connection.prepareStatement(INSERT_SUBSCRIPTION);
            preparedStatement = mapper.mapEntityToPreparedStatement(preparedStatement, entity);
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
            SubscriptionMapper mapper = new SubscriptionMapper();
            subscriptions = mapper.mapResultSetToEntity(resultSet);
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
            SubscriptionMapper mapper = new SubscriptionMapper();
            subscriptions = mapper.mapResultSetToEntity(resultSet);
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
            SubscriptionMapper mapper = new SubscriptionMapper();
            preparedStatement = mapper.mapEntityToPreparedStatement(preparedStatement, entity);
            preparedStatement.executeQuery();
            return true;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        } finally {
            close(preparedStatement);
        }
    }

}
