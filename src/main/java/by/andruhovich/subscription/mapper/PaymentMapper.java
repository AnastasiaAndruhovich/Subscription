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

public class PaymentMapper implements EntityMapper<Payment> {
    @Override
    public List<Payment> mapResultSetToEntity(ResultSet resultSet) throws DAOTechnicalException {
        List<Payment> subscriptions = new LinkedList<>();
        Payment payment;
        TypeConverter typeConverter = new TypeConverter();

        try {
            while (resultSet.next()) {
                int paymentNumber = resultSet.getInt("payment_number");
                BigDecimal sum = resultSet.getBigDecimal("sum");
                java.util.Date date = new java.util.Date(resultSet.getDate("date").getTime());
                boolean statement = typeConverter.convertStringToBoolean(resultSet.getString("subscription_is_active"));
                payment = new Payment(paymentNumber, sum, date, statement);
                subscriptions.add(payment);
            }
            return subscriptions;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        }
    }

    @Override
    public PreparedStatement mapEntityToPreparedStatement(PreparedStatement preparedStatement, Payment entity) throws DAOTechnicalException {
        TypeConverter typeConverter = new TypeConverter();

        Date date = new Date(entity.getDate().getTime());
        String statement = TypeConverter.convertBooleanToString(entity.isStatement());

        try {
            preparedStatement.setInt(1, entity.getUser().getUserId());
            preparedStatement.setInt(2, entity.getSubscription().getSubscriptionId());
            preparedStatement.setBigDecimal(3, entity.getSum());
            preparedStatement.setDate(4, date);
            preparedStatement.setString(5, statement);
            return preparedStatement;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        }
    }
}
