package by.andruhovich.subscription.dao;

import by.andruhovich.subscription.entity.Payment;

import java.sql.Connection;

public abstract class PaymentManagerDAO extends BaseDAO<Payment> {
    public PaymentManagerDAO(Connection connection) {
        super(connection);
    }
}
