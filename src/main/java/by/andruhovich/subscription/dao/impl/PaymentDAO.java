package by.andruhovich.subscription.dao.impl;

import by.andruhovich.subscription.dao.PaymentManagerDAO;
import by.andruhovich.subscription.entity.Payment;
import by.andruhovich.subscription.entity.Subscription;
import by.andruhovich.subscription.exception.DAOTechnicalException;
import by.andruhovich.subscription.mapper.PaymentMapper;
import by.andruhovich.subscription.mapper.SubscriptionMapper;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PaymentDAO extends PaymentManagerDAO {
    private static final String INSERT_PAYMENT = "INSERT INTO payments(subscription_id, sum, date, statement) " +
            "VALUES (?, ?, ?, ?)";
    private static final String SELECT_LAST_INSERT_ID = "SELECT payment_number FROM payments ORDER BY payment_number " +
            "DESC LIMIT 1";
    private static final String DELETE_PAYMENT_BY_ID = "DELETE FROM payments WHERE payment_number = ?";
    private static final String SELECT_COUNT = "SELECT COUNT(payment_number) AS count FROM payments";
    private static final String SELECT_PAYMENT_BY_ID = "SELECT payment_number, sum, date, statement FROM payments " +
            "WHERE payment_number = ?";
    private static final String SELECT_ALL_PAYMENTS = "SELECT payment_number, sum, date, statement FROM payments " +
            "ORDER BY payment_number DESC LIMIT ?, ?";
    private static final String UPDATE_PAYMENT = "UPDATE payments SET user_id = ?, subscription_id = ?, sum = ?, " +
            "date = ?, statement = ? WHERE payment_number = ?";

    private static final String SELECT_SUBSCRIPTION_BY_PAYMENT_NUMBER = "SELECT  s.subscription_id, s.start_date, " +
            "s.end_date, s.subscription_is_active FROM payments JOIN subscriptions s USING (subscription_id) " +
            "WHERE payment_number = ?";
    private static final String SELECT_PAYMENTS_BY_SUBSCRIPTION_ID = "SELECT payment_number, sum, date, statement " +
            "FROM payments WHERE subscription_id = ? ORDER BY subscription_id DESC LIMIT ?, ?";
    private static final String SELECT_PAYMENTS_BY_USER_ID = "SELECT p.payment_number, p.sum, p.date, p.statement " +
            "FROM payments p JOIN subscriptions s ON p.subscription_id = s.subscription_id WHERE s.user_id = ? " +
            "ORDER BY p.payment_number DESC LIMIT ?, ?";

    private static final Logger LOGGER = LogManager.getLogger(PaymentDAO.class);

    public PaymentDAO(Connection connection) {
        super(connection);
    }

    @Override
    public int create(Payment entity) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for create payment");
        PreparedStatement preparedStatement = null;
        PreparedStatement statement = null;
        PaymentMapper mapper = new PaymentMapper();
        int id = -1;

        try {
            preparedStatement = connection.prepareStatement(INSERT_PAYMENT);
            preparedStatement = mapper.mapEntityToPreparedStatement(preparedStatement, entity);
            preparedStatement.executeUpdate();
            statement = connection.prepareStatement(SELECT_LAST_INSERT_ID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                id = resultSet.getInt("payment_number");
            }
            LOGGER.log(Level.INFO, "Request for create payment - succeed");
            return id;
        } catch (MySQLIntegrityConstraintViolationException e) {
            LOGGER.log(Level.INFO, "Payment is already exist");
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
        LOGGER.log(Level.INFO, "Request for delete payment");
        return delete(id, DELETE_PAYMENT_BY_ID);
    }

    @Override
    public Payment findEntityById(int id) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for find entity by id");
        PreparedStatement preparedStatement = null;
        List<Payment> payments;

        try {
            preparedStatement = connection.prepareStatement(SELECT_PAYMENT_BY_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            PaymentMapper mapper = new PaymentMapper();
            payments = mapper.mapResultSetToEntity(resultSet);
            LOGGER.log(Level.INFO, "Request for find entity by id - succeed");
            if (payments.isEmpty()) {
                return null;
            }
            return payments.get(0);
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public List<Payment> findAll(int startIndex, int endIndex) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for find all");
        List<Payment> payments;
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SELECT_ALL_PAYMENTS);
            preparedStatement.setInt(1, startIndex);
            preparedStatement.setInt(2, endIndex);
            ResultSet resultSet = preparedStatement.executeQuery();
            PaymentMapper mapper = new PaymentMapper();
            payments = mapper.mapResultSetToEntity(resultSet);
            LOGGER.log(Level.INFO, "Request for find all - succeed");
            return payments;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public boolean update(Payment entity) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for update payment");
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(UPDATE_PAYMENT);
            PaymentMapper mapper = new PaymentMapper();
            preparedStatement = mapper.mapEntityToPreparedStatement(preparedStatement, entity);
            preparedStatement.executeUpdate();
            LOGGER.log(Level.INFO, "Request for update payment - succeed");
            return true;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public Subscription findSubscriptionByPaymentNumber(int id) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for find subscription by payment number");
        PreparedStatement preparedStatement = null;
        List<Subscription> subscriptions;

        try {
            preparedStatement = connection.prepareStatement(SELECT_SUBSCRIPTION_BY_PAYMENT_NUMBER);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            SubscriptionMapper subscriptionMapper = new SubscriptionMapper();
            subscriptions = subscriptionMapper.mapResultSetToEntity(resultSet);
            LOGGER.log(Level.INFO, "Request for find subscription by payment number - succeed");
            if (!subscriptions.isEmpty()) {
                return subscriptions.get(0);
            }
            return null;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public List<Payment> findPaymentsByUserId(int id, int startIndex, int endIndex) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for find payments by user id");
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SELECT_PAYMENTS_BY_USER_ID);
            preparedStatement.setInt(1, id);
            preparedStatement.setInt(2, startIndex);
            preparedStatement.setInt(3, endIndex);
            ResultSet resultSet = preparedStatement.executeQuery();
            LOGGER.log(Level.INFO, "Request for find payments by user id - succeed");
            PaymentMapper paymentMapper = new PaymentMapper();
            return paymentMapper.mapResultSetToEntity(resultSet);
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    public int findEntityCount() throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for get count");
        return findEntityCount(SELECT_COUNT);
    }
}
