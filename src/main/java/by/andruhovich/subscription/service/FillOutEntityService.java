package by.andruhovich.subscription.service;

import by.andruhovich.subscription.dao.*;
import by.andruhovich.subscription.dao.impl.*;
import by.andruhovich.subscription.entity.Author;
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

class FillOutEntityService {
    static List<Publication> fillOutPublicationList(List<Publication> publications) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;

        try {
            connection = connectionFactory.getConnection();
            PublicationManagerDAO publicationManagerDAO = new PublicationDAO(connection);
            AuthorPublicationManagerDAO authorPublicationManagerDAO = new AuthorPublicationDAO(connection);
            for (Publication publication : publications) {
                publication.setGenre(publicationManagerDAO.findGenreByPublicationId(publication.getPublicationId()));
                publication.setPublicationType(publicationManagerDAO.findPublicationTypeByPublicationId(publication.getPublicationId()));
                List<Author> authors = authorPublicationManagerDAO.findAuthorsByPublicationId(publication.getPublicationId());
                publication.setAuthors(correctAuthorList(authors));
            }
            return publications;
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    static List<Subscription> fillOutSubscriptionList(List<Subscription> subscriptions) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;
        List<Publication> publications = new LinkedList<>();

        try {
            connection = connectionFactory.getConnection();
            SubscriptionManagerDAO subscriptionManagerDAO = new SubscriptionDAO(connection);
            PublicationManagerDAO publicationManagerDAO = new PublicationDAO(connection);
            for (Subscription subscription : subscriptions) {
                subscription.setUser(subscriptionManagerDAO.findUserBySubscriptionId(subscription.getSubscriptionId()));
                Publication publication = publicationManagerDAO.findPublicationBySubscriptionId(subscription.getSubscriptionId());
                publications.add(publication);
                subscription.setPublication(fillOutPublicationList(publications).get(0));
                checkSubscriptionActive(subscription);
            }
            return subscriptions;
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    static List<User> fillOutUserList(List<User> users) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;
        int entityCount;

        try {
            connection = connectionFactory.getConnection();
            UserManagerDAO userManagerDAO = new UserDAO(connection);
            BlockManagerDAO blockManagerDAO = new BlockDAO(connection);
            SubscriptionManagerDAO subscriptionManagerDAO = new SubscriptionDAO(connection);
            for (User user : users) {
                user.setAccount(userManagerDAO.findAccountByUserId(user.getUserId()));
                user.setRole(userManagerDAO.findRoleByUserId(user.getUserId()));
                entityCount = subscriptionManagerDAO.findEntityCount();
                user.setSubscriptions(subscriptionManagerDAO.findSubscriptionsByUserId(user.getUserId(), 1, entityCount));
                user.setAdmin(blockManagerDAO.findAdminByUserId(user.getUserId()));
                entityCount = blockManagerDAO.findEntityCount();
                user.setUsers(blockManagerDAO.findUsersByAdminId(user.getUserId(), 1, entityCount));
            }
            return users;
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    private static Subscription checkSubscriptionActive(Subscription subscription) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;
        Date date = Calendar.getInstance().getTime();

        if (date.getTime() > subscription.getEndDate().getTime()) {
            try {
                connection = connectionFactory.getConnection();
                SubscriptionManagerDAO subscriptionManagerDAO = new SubscriptionDAO(connection);
                subscription.setSubscriptionIsActive(false);
                subscriptionManagerDAO.update(subscription);
            } catch (DAOTechnicalException | ConnectionTechnicalException e) {
                throw new ServiceTechnicalException(e);
            } finally {
                connectionFactory.returnConnection(connection);
            }
        }
        return subscription;
    }

    private static List<Author> correctAuthorList(List<Author> authors) {
        for (Author author : authors) {
            if ("-".equals(author.getAuthorFirstName())) {
                author.setAuthorFirstName(null);
            }
            if ("-".equals(author.getAuthorLastName())) {
                author.setAuthorLastName(null);
            }
        }
        return authors;
    }
}
