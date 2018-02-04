package by.andruhovich.subscription.service;

import by.andruhovich.subscription.dao.ConnectionFactory;
import by.andruhovich.subscription.dao.impl.PublicationDAO;
import by.andruhovich.subscription.dao.impl.SubscriptionDAO;
import by.andruhovich.subscription.dao.impl.UserDAO;
import by.andruhovich.subscription.entity.Publication;
import by.andruhovich.subscription.entity.Subscription;
import by.andruhovich.subscription.entity.User;
import by.andruhovich.subscription.exception.ConnectionTechnicalException;
import by.andruhovich.subscription.exception.DAOTechnicalException;
import by.andruhovich.subscription.exception.ServiceTechnicalException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

public class SubscriptionService extends BaseService{
    private static final Logger LOGGER = LogManager.getLogger(SubscriptionService.class);

    public List<Subscription> showSubscriptions(String pageNumber) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;

        int number = Integer.parseInt(pageNumber);
        int startIndex = (number - 1) * ENTITY_COUNT_FOR_ONE_PAGE;
        int endIndex = startIndex + ENTITY_COUNT_FOR_ONE_PAGE ;

        try {
            connection = connectionFactory.getConnection();
            SubscriptionDAO subscriptionDAO = new SubscriptionDAO(connection);
            List<Subscription> subscriptions =  subscriptionDAO.findAll(startIndex, endIndex);
            return fillOutSubscriptionList(subscriptions);
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    public int addSubscription(String login, Date startDate, Date endDate, String publicationId)
            throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;
        int intPublicationId = Integer.parseInt(publicationId);

        try {
            connection = connectionFactory.getConnection();
            connection.setAutoCommit(false);
            UserDAO userDAO = new UserDAO(connection);
            SubscriptionDAO subscriptionDAO = new SubscriptionDAO(connection);
            PublicationDAO publicationDAO = new PublicationDAO(connection);
            User user = userDAO.findUserByLogin(login);
            Publication publication = publicationDAO.findEntityById(intPublicationId);
            Subscription subscription = new Subscription(startDate, endDate, false, user, publication);
            int id = subscriptionDAO.create(subscription);
            connection.commit();
            return id;
        } catch (DAOTechnicalException | SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                LOGGER.log(Level.ERROR, "Error roll back transaction");
            }
            throw new ServiceTechnicalException(e);
        } catch (ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException e1) {
                    LOGGER.log(Level.ERROR, "Error set auto commit true");
                }
            }
            connectionFactory.returnConnection(connection);
        }
    }

    public boolean activateSubscription(String subscriptionId) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;
        int intSubscriptionId = Integer.parseInt(subscriptionId);

        try {
            connection = connectionFactory.getConnection();
            SubscriptionDAO subscriptionDAO = new SubscriptionDAO(connection);
            Subscription subscription = subscriptionDAO.findEntityById(intSubscriptionId);
            if (subscription != null) {
                subscription.setSubscriptionIsActive(true);
                return subscriptionDAO.update(subscription);
            }
            return false;
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    public boolean deactivateSubscription(String subscriptionId) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;
        int intSubscriptionId = Integer.parseInt(subscriptionId);

        try {
            connection = connectionFactory.getConnection();
            SubscriptionDAO subscriptionDAO = new SubscriptionDAO(connection);
            Subscription subscription = subscriptionDAO.findEntityById(intSubscriptionId);
            if (subscription != null) {
                subscription.setSubscriptionIsActive(false);
                return subscriptionDAO.update(subscription);
            }
            return false;
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    public int findSubscriptionPageCount() throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;

        try {
            connection = connectionFactory.getConnection();
            SubscriptionDAO subscriptionDAO = new SubscriptionDAO(connection);
            int count = subscriptionDAO.findEntityCount();
            return (int)Math.ceil((double)(count) / ENTITY_COUNT_FOR_ONE_PAGE);
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    private List<Subscription> fillOutSubscriptionList(List<Subscription> subscriptions) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;

        try {
            connection = connectionFactory.getConnection();
            SubscriptionDAO subscriptionDAO = new SubscriptionDAO(connection);
            for(Subscription subscription : subscriptions) {
                subscription.setUser(subscriptionDAO.findUserBySubscriptionId(subscription.getSubscriptionId()));
                subscription.setPublication(subscriptionDAO.findPublicationBySubscriptionId(subscription.getSubscriptionId()));
            }
            return subscriptions;
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }
}
