package by.andruhovich.subscription.dao;

import by.andruhovich.subscription.dao.ManagerDAO;
import by.andruhovich.subscription.entity.User;
import by.andruhovich.subscription.exception.DAOTechnicalException;

import java.sql.Connection;

public abstract class UserManagerDAO extends ManagerDAO<User> {
    public UserManagerDAO(Connection connection) {
        super(connection);
    }

    public abstract String findPasswordByLogin(String login) throws DAOTechnicalException;

    public abstract boolean isLoginExist(String login) throws DAOTechnicalException;
}
