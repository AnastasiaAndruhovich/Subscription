package by.andruhovich.subscription.dao.impl;

import by.andruhovich.subscription.dao.PaymentManagerDAO;
import by.andruhovich.subscription.entity.Payment;
import by.andruhovich.subscription.exception.DAOTechnicalException;
import by.andruhovich.subscription.mapper.PaymentMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PaymentDAO extends PaymentManagerDAO {
    private static final String INSERT_PAYMENT= "INSERT INTO payments(user_id, subscription_id, sum, date, statement) " +
            "VALUES (?, ?, ?, ?, ?)";
    private static final String DELETE_PAYMENT_BY_ID = "DELETE FROM payments WHERE payment_number = ?";
    private static final String SELECT_PAYMENT_BY_ID = "SELECT payment_number, sum, date, statement FROM payments " +
            "WHERE payment_number = ?";
    private static final String SELECT_ALL_PAYMENTS = "SELECT payment_number, sum, date, statement FROM payments";
    private static final String UPDATE_PAYMENT = "UPDATE payments SET user_id = ?, subscription_id = ?, sum = ?, " +
            "date = ?, statement = ? WHERE payment_number = ?";

    public PaymentDAO(Connection connection) {
        super(connection);
    }

    @Override
    public int create(Payment entity) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;
        PaymentMapper mapper = new PaymentMapper();
        int id = -1;

        try {
            preparedStatement = connection.prepareStatement(INSERT_PAYMENT);
            preparedStatement = mapper.mapEntityToPreparedStatement(preparedStatement, entity);
            preparedStatement.executeQuery();
            preparedStatement = connection.prepareStatement(SELECT_LAST_INSERT_ID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                id = resultSet.getInt("payment_id");
            }
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
            PaymentMapper mapper = new PaymentMapper();
            payments = mapper.mapResultSetToEntity(resultSet);
            if (payments.isEmpty()) {
                return null;
            }
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
            PaymentMapper mapper = new PaymentMapper();
            payments = mapper.mapResultSetToEntity(resultSet);
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
            PaymentMapper mapper = new PaymentMapper();
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
