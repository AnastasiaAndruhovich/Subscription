package by.andruhovich.subscription.dao;

import by.andruhovich.subscription.entity.User;
import by.andruhovich.subscription.exception.DAOTechnicalException;

public abstract class UserManagerDAO extends BaseDAO<User> {
    public abstract String findPasswordByLogin(String login) throws DAOTechnicalException;
}
