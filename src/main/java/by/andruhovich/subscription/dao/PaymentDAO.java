package by.andruhovich.subscription.dao;

import by.andruhovich.subscription.entity.Payment;
import by.andruhovich.subscription.exception.DAOTechnicalException;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class PaymentDAO extends PaymentManagerDAO {
    private static final String INSERT_PAYMENT= "INSERT INTO payments(user_id, subscription_id, sum, date, statement) " +
            "VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_LAST_INSERT_ID = "SELECT LAST_INSERT_ID()";
    private static final String DELETE_PAYMENT_BY_ID = "DELETE FROM payments WHERE payment_number = ?";
    private static final String SELECT_PAYMENT_BY_ID = "SELECT * FROM payments WHERE payment_number = ?";
    private static final String SELECT_ALL_PAYMENTS = "SELECT * FROM payments";
    private static final String UPDATE_PAYMENT = "UPDATE payments SET user_id = ?, subscription_id = ?, sum = ?, " +
            "date = ?, statement = ? WHERE payment_number = ?";

    public PaymentDAO(Connection connection) {
        super(connection);
    }

    @Override
    public int create(Payment entity) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(INSERT_PAYMENT);
            preparedStatement = fillOutStatementByPayment(preparedStatement, entity);
            preparedStatement.executeQuery();
            preparedStatement = connection.prepareStatement(SELECT_LAST_INSERT_ID);
            ResultSet resultSet = preparedStatement.executeQuery();
            int id = resultSet.getInt("payment_id");
            return id;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public boolean delete(Payment entity) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(DELETE_PAYMENT_BY_ID);
            preparedStatement.setInt(1, entity.getPaymentNumber());
            preparedStatement.executeQuery();
            return true;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public Payment findEntityById(int id) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;
        List<Payment> payments;

        try {
            preparedStatement = connection.prepareStatement(SELECT_PAYMENT_BY_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            payments = createPaymentList(resultSet);
            return payments.get(0);
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public List<Payment> findAll() throws DAOTechnicalException {
        List<Payment> payments;
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SELECT_ALL_PAYMENTS);
            ResultSet resultSet = preparedStatement.executeQuery();
            payments = createPaymentList(resultSet);
            return payments;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public boolean update(Payment entity) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(UPDATE_PAYMENT);
            preparedStatement = fillOutStatementByPayment(preparedStatement, entity);
            preparedStatement.executeQuery();
            return true;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        } finally {
            close(preparedStatement);
        }
    }

    private PreparedStatement fillOutStatementByPayment(PreparedStatement preparedStatement, Payment payment)
            throws DAOTechnicalException {
        TypeConverter typeConverter = new TypeConverter();

        java.sql.Date date = new java.sql.Date(payment.getDate().getTime());
        String statement = typeConverter.convertBooleanToString(payment.getStatement());

        try {
            preparedStatement.setInt(1, payment.getUserId());
            preparedStatement.setInt(2, payment.getSubscriptionId());
            preparedStatement.setBigDecimal(3, payment.getSum());
            preparedStatement.setDate(4, date);
            preparedStatement.setString(5, statement);
            return preparedStatement;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        }
    }

    private List<Payment> createPaymentList(ResultSet resultSet) throws DAOTechnicalException {
        List<Payment> subscriptions = new LinkedList<>();
        Payment payment;
        TypeConverter typeConverter = new TypeConverter();

        try {
            while (resultSet.next()) {
                int paymentNumber = resultSet.getInt("payment_number");
                int userId = resultSet.getInt("user_id");
                int subscriptionId = resultSet.getInt("subscription_id");
                BigDecimal sum = new BigDecimal("sum");
                java.util.Date date = new java.util.Date(resultSet.getDate("date").getTime());
                boolean statement = typeConverter.convertStringToBoolean(resultSet.getString("subscription_is_active"));
                payment = new Payment(paymentNumber, userId, subscriptionId, sum, date, statement);
                subscriptions.add(payment);
            }
            return subscriptions;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        }
    }
}
