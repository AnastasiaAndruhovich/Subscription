package by.andruhovich.subscription.mapper;

import by.andruhovich.subscription.entity.Payment;
import by.andruhovich.subscription.exception.DAOTechnicalException;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Provides methods to prepare Payment entity for setting and getting from database
 */
public class PaymentMapper implements EntityMapper<Payment> {
    /**
     * @param resultSet java.sql.ResultSet from database to map on entity
     * @return Payment list from resultSet
     * @throws DAOTechnicalException
     *          If there was an error during mapping resultSet
     */
    @Override
    public List<Payment> mapResultSetToEntity(ResultSet resultSet) throws DAOTechnicalException {
        List<Payment> subscriptions = new LinkedList<>();
        Payment payment;

        try {
            while (resultSet.next()) {
                int paymentNumber = resultSet.getInt("payment_number");
                BigDecimal sum = resultSet.getBigDecimal("sum");
                java.util.Date date = new java.util.Date(resultSet.getDate("date").getTime());
                boolean statement = TypeConverter.convertStringToBoolean(resultSet.getString("statement"));
                payment = new Payment(paymentNumber, sum, date, statement);
                subscriptions.add(payment);
            }
            return subscriptions;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        }
    }

    /**
     * @param preparedStatement java.sql.Statement with all necessary parameters
     * @param entity Payment to be set in database
     * @return Filled out statement by entity
     * @throws DAOTechnicalException
     *          If there was an error during mapping resultSet
     */
    @Override
    public PreparedStatement mapEntityToPreparedStatement(PreparedStatement preparedStatement, Payment entity) throws DAOTechnicalException {
        Date date = new Date(entity.getDate().getTime());
        String statement = TypeConverter.convertBooleanToString(entity.isStatement());

        try {
            preparedStatement.setInt(1, entity.getSubscription().getSubscriptionId());
            preparedStatement.setBigDecimal(2, entity.getSum());
            preparedStatement.setDate(3, date);
            preparedStatement.setString(4, statement);
            return preparedStatement;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        }
    }
}
