package by.andruhovich.subscription.dao;

import by.andruhovich.subscription.entity.Account;
import by.andruhovich.subscription.entity.Role;
import by.andruhovich.subscription.entity.Subscription;
import by.andruhovich.subscription.entity.User;
import by.andruhovich.subscription.exception.DAOTechnicalException;

import java.sql.Connection;
import java.util.List;

public abstract class UserManagerDAO extends MediatorManagerDAO<User> {
    public UserManagerDAO(Connection connection) {
        super(connection);
    }

    public abstract User findUserByAccountNumber(int id) throws DAOTechnicalException;
    public abstract List<User> findUsersByRoleId(int id) throws DAOTechnicalException;
    public abstract Role findRoleByUserId(int id) throws DAOTechnicalException;
    public abstract Account findAccountByUserId(int id) throws DAOTechnicalException;
    public abstract List<Subscription> findSubscriptionsByUserId(int id) throws DAOTechnicalException;
    public abstract User findBlockedAdminByUserId(int id) throws DAOTechnicalException;
    public abstract List<User> findBlockedUsersByAdminId(int id) throws DAOTechnicalException;

    public abstract String findPasswordByLogin(String login) throws DAOTechnicalException;
    public abstract boolean isLoginExist(String login) throws DAOTechnicalException;
}
