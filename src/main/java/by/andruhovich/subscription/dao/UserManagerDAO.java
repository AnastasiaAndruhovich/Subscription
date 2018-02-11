package by.andruhovich.subscription.dao;

import by.andruhovich.subscription.entity.Account;
import by.andruhovich.subscription.entity.Role;
import by.andruhovich.subscription.entity.User;
import by.andruhovich.subscription.exception.DAOTechnicalException;

import java.sql.Connection;

/**
 * Concrete Manager extends ManagerDAO parametrize by User entity
 */
public abstract class UserManagerDAO extends ManagerDAO<User> {
    /**
     * @param connection java.sql.Connection to initialize super class
     */
    public UserManagerDAO(Connection connection) {
        super(connection);
    }

    /**
     * @param id User id in database
     * @return Role relevant to user in database
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    public abstract Role findRoleByUserId(int id) throws DAOTechnicalException;

    /**
     * @param id User id in database
     * @return Account relevant to user in database
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    public abstract Account findAccountByUserId(int id) throws DAOTechnicalException;

    /**
     * @param id User id in database
     * @return String password relevant to user in database
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    public abstract String findPasswordById(int id) throws DAOTechnicalException;

    /**
     * @param login String login in database
     * @return User id relevant to login in database
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    public abstract int findUserIdByLogin(String login) throws DAOTechnicalException;
}
