package by.andruhovich.subscription.service;

import by.andruhovich.subscription.dao.PublicationManagerDAO;
import by.andruhovich.subscription.dao.SubscriptionManagerDAO;
import by.andruhovich.subscription.dao.UserManagerDAO;
import by.andruhovich.subscription.dao.impl.PublicationDAO;
import by.andruhovich.subscription.dao.impl.SubscriptionDAO;
import by.andruhovich.subscription.dao.impl.UserDAO;
import by.andruhovich.subscription.entity.Publication;
import by.andruhovich.subscription.entity.Subscription;
import by.andruhovich.subscription.entity.User;
import by.andruhovich.subscription.exception.ConnectionTechnicalException;
import by.andruhovich.subscription.exception.DAOTechnicalException;
import by.andruhovich.subscription.exception.ServiceTechnicalException;
import by.andruhovich.subscription.pool.ConnectionFactory;

import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Provides methods to process subscription information
 */
public class SubscriptionService extends BaseService {
    /**
     * @param pageNumber Current page number from jsp
     * @return Subscription list from database
     * @throws ServiceTechnicalException
     *          If there was an error during processing operation
     */
    public List<Subscription> showSubscriptions(String pageNumber) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;
        int page = Integer.parseInt(pageNumber);

        try {
            connection = connectionFactory.getConnection();
            SubscriptionManagerDAO subscriptionManagerDAO = new SubscriptionDAO(connection);
            int startIndex = (page - 1) * ENTITY_COUNT_FOR_ONE_PAGE;
            int endIndex = startIndex + ENTITY_COUNT_FOR_ONE_PAGE;
            List<Subscription> subscriptions = subscriptionManagerDAO.findAll(startIndex, endIndex);
            return FillOutEntityService.fillOutSubscriptionList(subscriptions);
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    /**
     * @param userId User id
     * @param publicationId Publication id
     * @return Relevant subscription from database
     * @throws ServiceTechnicalException
     *          If there was an error during processing operation
     */
    public Subscription addSubscription(String userId, String publicationId)
            throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;
        int intPublicationId = Integer.parseInt(publicationId);
        int intUserId = Integer.parseInt(userId);
        List<Subscription> subscriptions = new LinkedList<>();

        Date startDate = defineStartDate();
        Date endDate = defineEndDate(startDate);

        try {
            connection = connectionFactory.getConnection();
            UserManagerDAO userManagerDAO = new UserDAO(connection);
            SubscriptionManagerDAO subscriptionManagerDAO = new SubscriptionDAO(connection);
            PublicationManagerDAO publicationManagerDAO = new PublicationDAO(connection);
            User user = userManagerDAO.findEntityById(intUserId);
            Publication publication = publicationManagerDAO.findEntityById(intPublicationId);
            Subscription subscription = new Subscription(startDate, endDate, false, user, publication);
            int id = subscriptionManagerDAO.create(subscription);
            subscription = subscriptionManagerDAO.findEntityById(id);
            subscriptions.add(subscription);
            return FillOutEntityService.fillOutSubscriptionList(subscriptions).get(0);
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    /**
     * @param subscriptionId Subscription id
     * @return {@code true} if the operation has been completed successfully
     *         {@code false} otherwise
     * @throws ServiceTechnicalException
     *          If there was an error during processing operation
     */
    public boolean deleteSubscription(String subscriptionId) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;
        int intSubscriptionId = Integer.parseInt(subscriptionId);

        try {
            connection = connectionFactory.getConnection();
            SubscriptionManagerDAO subscriptionManagerDAO = new SubscriptionDAO(connection);
            Subscription subscription = subscriptionManagerDAO.findEntityById(intSubscriptionId);
            return !subscription.isSubscriptionIsActive() && subscriptionManagerDAO.delete(intSubscriptionId);
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    /**
     * @param subscriptionId Subscription id
     * @return Relevant subscription from database
     * @throws ServiceTechnicalException
     *          If there was an error during processing operation
     */
    public Subscription findSubscriptionById(String subscriptionId) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;

        int id = Integer.parseInt(subscriptionId);

        try {
            connection = connectionFactory.getConnection();
            SubscriptionManagerDAO subscriptionManagerDAO = new SubscriptionDAO(connection);
            Subscription subscription = subscriptionManagerDAO.findEntityById(id);
            List<Subscription> subscriptions = new LinkedList<>();
            subscriptions.add(subscription);
            return FillOutEntityService.fillOutSubscriptionList(subscriptions).get(0);
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    /**
     * @return SubscriptionPageCount from database
     * @throws ServiceTechnicalException
     *          If there was an error during processing operation
     */
    public int findSubscriptionPageCount() throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;

        try {
            connection = connectionFactory.getConnection();
            SubscriptionDAO subscriptionDAO = new SubscriptionDAO(connection);
            int count = subscriptionDAO.findEntityCount();
            return (int) Math.ceil((double) (count) / ENTITY_COUNT_FOR_ONE_PAGE);
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    /**
     * @param userId User id
     * @param pageNumber Current page number
     * @return Relevant subscription list from database
     * @throws ServiceTechnicalException
     *          If there was an error during processing operation
     */
    public List<Subscription> findSubscriptionByUserId(String userId, String pageNumber) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;
        int id = Integer.parseInt(userId);
        int page = Integer.parseInt(pageNumber);

        try {
            connection = connectionFactory.getConnection();
            SubscriptionManagerDAO subscriptionManagerDAO = new SubscriptionDAO(connection);
            int startIndex = (page - 1) * ENTITY_COUNT_FOR_ONE_PAGE;
            List<Subscription> subscriptions = subscriptionManagerDAO.findSubscriptionsByUserId(id, startIndex, ENTITY_COUNT_FOR_ONE_PAGE);
            return FillOutEntityService.fillOutSubscriptionList(subscriptions);
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    /**
     * @param userId User id
     * @return Relevant Subscription
     * @throws ServiceTechnicalException
     *          If there was an error during processing operation
     */
    public int findSubscriptionByUserPageCount(String userId) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;
        int id = Integer.parseInt(userId);

        try {
            connection = connectionFactory.getConnection();
            SubscriptionManagerDAO subscriptionManagerDAO = new SubscriptionDAO(connection);
            int count = subscriptionManagerDAO.findSubscriptionCountByUserId(id);
            return (int) Math.ceil((double) (count) / ENTITY_COUNT_FOR_ONE_PAGE);
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    /**
     * @return Calculated subscription start date
     */
    private Date defineStartDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 1);
        Calendar date = Calendar.getInstance();
        date.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), 1);
        return date.getTime();
    }

    /**
     * @param date Subscription start date
     * @return Calculated subscription end date
     */
    private Date defineEndDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, 1);
        return calendar.getTime();
    }
}