package by.andruhovich.subscription.dao;

import by.andruhovich.subscription.entity.Subscription;

import java.sql.Connection;

public abstract class SubscriptionManagerDAO extends BaseDAO<Subscription> {
    public SubscriptionManagerDAO(Connection connection) {
        super(connection);
    }
}
