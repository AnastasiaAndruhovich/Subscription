package by.andruhovich.subscription.dao;

import by.andruhovich.subscription.entity.Account;
import by.andruhovich.subscription.entity.Role;
import by.andruhovich.subscription.entity.User;
import by.andruhovich.subscription.exception.DAOTechnicalException;

import java.sql.Connection;

public abstract class UserManagerDAO extends ManagerDAO<User> {
    public UserManagerDAO(Connection connection) {
        super(connection);
    }

    public abstract User findUserByAccountNumber(int id) throws DAOTechnicalException;
    public abstract Role findRoleByUserId(int id) throws DAOTechnicalException;
    public abstract Account findAccountByUserId(int id) throws DAOTechnicalException;

    public abstract String findPasswordById(int id) throws DAOTechnicalException;
    public abstract int findUserIdByLogin(String login) throws DAOTechnicalException;
}
