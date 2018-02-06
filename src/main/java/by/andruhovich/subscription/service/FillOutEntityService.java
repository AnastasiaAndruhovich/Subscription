package by.andruhovich.subscription.service;

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
import java.util.LinkedList;
import java.util.List;

class FillOutEntityService {
    static List<Publication> fillOutPublicationList(List<Publication> publications) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;

        try {
            connection = connectionFactory.getConnection();
            PublicationDAO publicationDAO = new PublicationDAO(connection);
            AuthorPublicationDAO authorPublicationDAO = new AuthorPublicationDAO(connection);
            for (Publication publication : publications) {
                publication.setGenre(publicationDAO.findGenreByPublicationId(publication.getPublicationId()));
                publication.setPublicationType(publicationDAO.findPublicationTypeByPublicationId(publication.getPublicationId()));
                List<Author> authors = authorPublicationDAO.findAuthorsByPublicationId(publication.getPublicationId());
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
        PublicationService publicationService = new PublicationService();

        try {
            connection = connectionFactory.getConnection();
            SubscriptionDAO subscriptionDAO = new SubscriptionDAO(connection);
            PublicationDAO publicationDAO = new PublicationDAO(connection);
            for (Subscription subscription : subscriptions) {
                subscription.setUser(subscriptionDAO.findUserBySubscriptionId(subscription.getSubscriptionId()));
                Publication publication = publicationDAO.findPublicationBySubscriptionId(subscription.getSubscriptionId());
                publications.add(publication);
                subscription.setPublication(fillOutPublicationList(publications).get(0));
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

        try {
            connection = connectionFactory.getConnection();
            UserDAO userDAO = new UserDAO(connection);
            BlockDAO blockDAO = new BlockDAO(connection);
            SubscriptionDAO subscriptionDAO = new SubscriptionDAO(connection);
            for (User user : users) {
                user.setAccount(userDAO.findAccountByUserId(user.getUserId()));
                user.setRole(userDAO.findRoleByUserId(user.getUserId()));
                user.setSubscriptions(subscriptionDAO.findSubscriptionsByUserId(user.getUserId()));
                user.setAdmin(blockDAO.findAdminByUserId(user.getUserId()));
                user.setUsers(blockDAO.findUsersByAdminId(user.getUserId()));
            }
            return users;
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
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
