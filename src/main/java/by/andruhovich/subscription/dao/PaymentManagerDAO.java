package by.andruhovich.subscription.dao;

import by.andruhovich.subscription.entity.Payment;
import by.andruhovich.subscription.entity.Subscription;
import by.andruhovich.subscription.exception.DAOTechnicalException;

import java.sql.Connection;
import java.util.List;

/**
 * Concrete Manager extends ManagerDAO parametrize by Payment entity
 */
public abstract class PaymentManagerDAO extends ManagerDAO<Payment> {

    /**
     * @param connection java.sql.Connection to initialize super class
     */
    public PaymentManagerDAO(Connection connection) {
        super(connection);
    }

    /**
     * @param id Payment id in database
     * @return  Subscription relevant to payment id
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    public abstract Subscription findSubscriptionByPaymentNumber(int id) throws DAOTechnicalException;

    /**
     * @param userId User id in database
     * @param startIndex Payment start index in database
     * @param endIndex Payment end index in database
     * @return Payment list relevant to user id in database
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    public abstract List<Payment> findPaymentsByUserId(int userId, int startIndex, int endIndex) throws DAOTechnicalException;
}
