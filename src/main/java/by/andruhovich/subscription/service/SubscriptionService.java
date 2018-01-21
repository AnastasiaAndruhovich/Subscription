package by.andruhovich.subscription.service;

import by.andruhovich.subscription.dao.DAOFactory;
import by.andruhovich.subscription.dao.impl.PublicationDAO;
import by.andruhovich.subscription.dao.impl.SubscriptionDAO;
import by.andruhovich.subscription.dao.impl.UserDAO;
import by.andruhovich.subscription.entity.Publication;
import by.andruhovich.subscription.entity.Subscription;
import by.andruhovich.subscription.entity.User;
import by.andruhovich.subscription.exception.ConnectionTechnicalException;
import by.andruhovich.subscription.exception.DAOTechnicalException;
import by.andruhovich.subscription.exception.ServiceTechnicalException;

import java.util.Date;
import java.util.List;

public class SubscriptionService {
    private static final int ENTITY_QUANTITY_FOR_ONE_PAGE = 10;

    public List<Subscription> showSubscriptions(String pageNumber) throws ServiceTechnicalException {
        DAOFactory daoFactory = DAOFactory.getInstance();
        SubscriptionDAO subscriptionDAO = null;

        int number = Integer.parseInt(pageNumber);
        int startIndex = (number - 1) * ENTITY_QUANTITY_FOR_ONE_PAGE;
        int endIndex = startIndex + ENTITY_QUANTITY_FOR_ONE_PAGE ;

        try {
            subscriptionDAO = daoFactory.createSubscriptionDAO();
            List<Subscription> subscriptions =  subscriptionDAO.findAll(startIndex, endIndex);
            return fillOutSubscriptionList(subscriptions);
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            daoFactory.closeDAO(subscriptionDAO);
        }
    }

    public int addSubscription(String login, Date startDate, Date endDate, String publicationId)
            throws ServiceTechnicalException {
        DAOFactory daoFactory = DAOFactory.getInstance();
        SubscriptionDAO subscriptionDAO = null;
        UserDAO userDAO = null;
        PublicationDAO publicationDAO = null;
        int intPublicationId = Integer.parseInt(publicationId);

        try {
            userDAO = daoFactory.createUserDAO();
            subscriptionDAO = daoFactory.createSubscriptionDAO();
            publicationDAO = daoFactory.createPublicationDAO();
            User user = userDAO.findUserByLogin(login);
            Publication publication = publicationDAO.findEntityById(intPublicationId);
            Subscription subscription = new Subscription(startDate, endDate, false, user, publication);
            return subscriptionDAO.create(subscription);
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            daoFactory.closeDAO(userDAO);
            daoFactory.closeDAO(subscriptionDAO);
            daoFactory.closeDAO(publicationDAO);
        }
    }

    public boolean activateSubscription(String subscriptionId) throws ServiceTechnicalException {
        DAOFactory daoFactory = DAOFactory.getInstance();
        SubscriptionDAO subscriptionDAO = null;
        int intSubscriptionId = Integer.parseInt(subscriptionId);

        try {
            subscriptionDAO = daoFactory.createSubscriptionDAO();
            Subscription subscription = subscriptionDAO.findEntityById(intSubscriptionId);
            if (subscription != null) {
                subscription.setSubscriptionIsActive(true);
                return subscriptionDAO.update(subscription);
            }
            return false;
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        }
    }

    public boolean deactivateSubscription(String subscriptionId) throws ServiceTechnicalException {
        DAOFactory daoFactory = DAOFactory.getInstance();
        SubscriptionDAO subscriptionDAO = null;
        int intSubscriptionId = Integer.parseInt(subscriptionId);

        try {
            subscriptionDAO = daoFactory.createSubscriptionDAO();
            Subscription subscription = subscriptionDAO.findEntityById(intSubscriptionId);
            if (subscription != null) {
                subscription.setSubscriptionIsActive(false);
                return subscriptionDAO.update(subscription);
            }
            return false;
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        }
    }

    private List<Subscription> fillOutSubscriptionList(List<Subscription> subscriptions) throws ServiceTechnicalException {
        DAOFactory daoFactory = DAOFactory.getInstance();
        SubscriptionDAO subscriptionDAO = null;

        try {
            subscriptionDAO = daoFactory.createSubscriptionDAO();
            for(Subscription subscription : subscriptions) {
                subscription.setUser(subscriptionDAO.findUserBySubscriptionId(subscription.getSubscriptionId()));
                subscription.setPublication(subscriptionDAO.findPublicationBySubscriptionId(subscription.getSubscriptionId()));
            }
            return subscriptions;
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            daoFactory.closeDAO(subscriptionDAO);
        }
    }
}
