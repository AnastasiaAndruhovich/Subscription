package by.andruhovich.subscription.dao;

import by.andruhovich.subscription.entity.User;

import java.sql.Connection;

public abstract class UserManagerDAO extends BaseDAO<User> {
    public UserManagerDAO(Connection connection) {
        super(connection);
    }

    public abstract String findPasswordByLogin(String login);
}
