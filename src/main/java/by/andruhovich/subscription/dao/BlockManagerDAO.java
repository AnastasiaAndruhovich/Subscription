package by.andruhovich.subscription.dao;

import by.andruhovich.subscription.entity.Block;
import by.andruhovich.subscription.entity.User;
import by.andruhovich.subscription.exception.DAOTechnicalException;

import java.sql.Connection;
import java.util.List;

/**
 * Concrete Manager extends ManagerDAO parametrize by Block entity
 */
public abstract class BlockManagerDAO extends ManagerDAO<Block> {

    /**
     * @param connection java.sql.Connection to initialize super class
     */
    public BlockManagerDAO(Connection connection) {
        super(connection);
    }

    /**
     * @param id User id in database
     * @return {@code true} if the operation has been completed successfully
     *         {@code false} otherwise
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    public abstract boolean deleteBlockByUserId(int id) throws DAOTechnicalException;

    /**
     * @param adminId User id with admin role in database
     * @return Blocked user count relevant to user id in database
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    public abstract int findBlockedUsersCountByAdminId(int adminId) throws DAOTechnicalException;

    /**
     * @param id User id with admin role in database
     * @param startIndex User start index relevant to user-admin id in database
     * @param endIndex User end index relevant to user-admin id in database
     * @return Blocked user list relevant to user id in database
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    public abstract List<User> findUsersByAdminId(int id, int startIndex, int endIndex) throws DAOTechnicalException;

    /**
     * @param id User id in database
     * @return  User with admin role blocked user with relevant id
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    public abstract User findAdminByUserId(int id) throws DAOTechnicalException;
}
