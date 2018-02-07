package by.andruhovich.subscription.dao;

import by.andruhovich.subscription.entity.Subscription;
import by.andruhovich.subscription.entity.User;
import by.andruhovich.subscription.exception.DAOTechnicalException;

import java.sql.Connection;
import java.util.List;

public abstract class SubscriptionManagerDAO extends MediatorManagerDAO<Subscription> {
    public SubscriptionManagerDAO(Connection connection) {
        super(connection);
    }

    public abstract User findUserBySubscriptionId(int id) throws DAOTechnicalException;
    public abstract List<Subscription> findSubscriptionsByUserId(int id) throws DAOTechnicalException;
}
