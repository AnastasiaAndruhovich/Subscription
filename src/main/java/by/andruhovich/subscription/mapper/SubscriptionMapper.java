package by.andruhovich.subscription.mapper;

import by.andruhovich.subscription.exception.DAOTechnicalException;
import by.andruhovich.subscription.entity.Subscription;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class SubscriptionMapper implements EntityMapper<Subscription> {
    @Override
    public List<Subscription> mapResultSetToEntity(ResultSet resultSet) throws DAOTechnicalException {
        List<Subscription> subscriptions = new LinkedList<>();
        Subscription subscription;
        TypeConverter typeConverter = new TypeConverter();

        try {
            while (resultSet.next()) {
                int subscriptionId = resultSet.getInt("subscription_id");
                java.util.Date startDate = new java.util.Date(resultSet.getDate("start_date").getTime());
                java.util.Date endDate = new java.util.Date(resultSet.getDate("end_date").getTime());
                boolean subscriptionIsActive = typeConverter.convertStringToBoolean(resultSet.getString("subscription_is_active"));
                subscription = new Subscription(subscriptionId, startDate, endDate, subscriptionIsActive);
                subscriptions.add(subscription);
            }
            return subscriptions;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        }
    }

    @Override
    public PreparedStatement mapEntityToPreparedStatement(PreparedStatement preparedStatement, Subscription entity) throws DAOTechnicalException {
        Date startDate = new Date(entity.getStartDate().getTime());
        Date endDate = new Date(entity.getEndDate().getTime());
        String subscriptionIsActive = TypeConverter.convertBooleanToString(entity.isSubscriptionIsActive());

        try {
            preparedStatement.setInt(1, entity.getUser().getUserId());
            preparedStatement.setInt(2, entity.getPublication().getPublicationId());
            preparedStatement.setDate(3, startDate);
            preparedStatement.setDate(4, endDate);
            preparedStatement.setString(5, subscriptionIsActive);
            return preparedStatement;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        }
    }
}
