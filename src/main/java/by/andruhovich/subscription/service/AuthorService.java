package by.andruhovich.subscription.service;

import by.andruhovich.subscription.dao.AuthorManagerDAO;
import by.andruhovich.subscription.dao.AuthorPublicationManagerDAO;
import by.andruhovich.subscription.dao.impl.AuthorDAO;
import by.andruhovich.subscription.dao.impl.AuthorPublicationDAO;
import by.andruhovich.subscription.entity.Author;
import by.andruhovich.subscription.exception.ConnectionTechnicalException;
import by.andruhovich.subscription.exception.DAOTechnicalException;
import by.andruhovich.subscription.exception.ServiceTechnicalException;
import by.andruhovich.subscription.pool.ConnectionFactory;

import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;

public class AuthorService extends BaseService{
    public int addAuthor(String authorFirstName, String authorLastName, String publisherName)
            throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;
        Author author = new Author(publisherName, authorLastName ,authorFirstName);

        try {
            connection = connectionFactory.getConnection();
            AuthorManagerDAO authorManagerDAO = new AuthorDAO(connection);
            return authorManagerDAO.create(author);
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    public boolean updateAuthor(String id, String authorLastName, String authorFirstName, String publisherName)
            throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;
        int intId = Integer.parseInt(id);
        Author author = new Author(intId, publisherName, authorLastName ,authorFirstName);

        try {
            connection = connectionFactory.getConnection();
            AuthorManagerDAO authorManagerDAO = new AuthorDAO(connection);
            return authorManagerDAO.update(author);
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    public boolean deleteAuthor(String authorId) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;
        int intAuthorId = Integer.parseInt(authorId);

        try {
            connection = connectionFactory.getConnection();
            AuthorManagerDAO authorManagerDAO = new AuthorDAO(connection);
            AuthorPublicationManagerDAO authorPublicationManagerDAO = new AuthorPublicationDAO(connection);
            /*authorPublicationDAO = connectionFactory.createAuthorPublicationDAO();
            return authorPublicationDAO.deletePublicationsByAuthorId(intAuthorId);*/
            return authorManagerDAO.delete(intAuthorId);
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    public int findAuthorPageCount() throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;

        try {
            connection = connectionFactory.getConnection();
            AuthorDAO authorDAO = new AuthorDAO(connection);
            int count = authorDAO.findEntityCount();
            return (int)Math.ceil((double)(count) / ENTITY_COUNT_FOR_ONE_PAGE);
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    public List<Author> showAuthors(String pageNumber) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;

        try {
            connection = connectionFactory.getConnection();
            AuthorManagerDAO authorManagerDAO = new AuthorDAO(connection);
            int entityCount = authorManagerDAO.findEntityCount();
            int startIndex = findStartIndex(pageNumber, entityCount);
            int endIndex = startIndex + ENTITY_COUNT_FOR_ONE_PAGE;
            return authorManagerDAO.findAll(startIndex, endIndex);
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    public List<Author> createAuthorList(List<String> authorFirstNames, List<String> authorLastNames,
                                         String publisherName) throws ServiceTechnicalException {
        List<Author> authors = new LinkedList<>();

        for (int i = 0; i < authorFirstNames.size(); i++) {
            int authorId = findIdByAuthorName(authorFirstNames.get(i), authorLastNames.get(i), publisherName);
            if (authorId == -1) {
                authorId = addAuthor(authorFirstNames.get(i), authorLastNames.get(i), publisherName);
            }
            Author author = new Author(authorId, publisherName, authorLastNames.get(i), authorFirstNames.get(i));
            authors.add(author);
        }
        return authors;
    }

    private int findIdByAuthorName(String authorFirstName, String authorLastName, String publisherName)
            throws ServiceTechnicalException {
        Author author = new Author(publisherName, authorLastName, authorFirstName);
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;

        try {
            connection = connectionFactory.getConnection();
            AuthorManagerDAO authorManagerDAO = new AuthorDAO(connection);
            return authorManagerDAO.findIdByEntity(author);
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }

    }
}
