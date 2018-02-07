package by.andruhovich.subscription.dao;

import by.andruhovich.subscription.entity.Payment;
import by.andruhovich.subscription.entity.Subscription;
import by.andruhovich.subscription.exception.DAOTechnicalException;

import java.sql.Connection;
import java.util.List;

public abstract class PaymentManagerDAO extends ManagerDAO<Payment> {
    public PaymentManagerDAO(Connection connection) {
        super(connection);
    }

    public abstract Subscription findSubscriptionByPaymentNumber(int id) throws DAOTechnicalException;
    public abstract List<Payment> findPaymentsBySubscriptionId(int id) throws DAOTechnicalException;
}
