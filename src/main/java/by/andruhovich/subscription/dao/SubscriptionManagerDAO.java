package by.andruhovich.subscription.dao;

import by.andruhovich.subscription.entity.Subscription;
import by.andruhovich.subscription.entity.User;
import by.andruhovich.subscription.exception.DAOTechnicalException;

import java.sql.Connection;
import java.util.List;

/**
 * Concrete Manager extends ManagerDAO parametrize by Subscription entity
 */
public abstract class SubscriptionManagerDAO extends ManagerDAO<Subscription> {
    /**
     * @param connection java.sql.Connection to initialize super class
     */
    public SubscriptionManagerDAO(Connection connection) {
        super(connection);
    }

    /**
     * @param userId User id in database
     * @return Subscription count relevant to user in database
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    public abstract int findSubscriptionCountByUserId(int userId) throws DAOTechnicalException;

    /**
     * @param id Subscription id in database
     * @return User relevant to subscription in database
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    public abstract User findUserBySubscriptionId(int id) throws DAOTechnicalException;

    /**
     * @param id User id in database
     * @param startIndex Subscription start index in database
     * @param endIndex Subscription end index in database
     * @return Subscription list relevant to user in database
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    public abstract List<Subscription> findSubscriptionsByUserId(int id, int startIndex, int endIndex) throws DAOTechnicalException;
}
