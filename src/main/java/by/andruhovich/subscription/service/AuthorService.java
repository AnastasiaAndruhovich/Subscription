package by.andruhovich.subscription.service;

import by.andruhovich.subscription.dao.AuthorManagerDAO;
import by.andruhovich.subscription.dao.AuthorPublicationManagerDAO;
import by.andruhovich.subscription.dao.PublicationManagerDAO;
import by.andruhovich.subscription.dao.impl.AuthorDAO;
import by.andruhovich.subscription.dao.impl.AuthorPublicationDAO;
import by.andruhovich.subscription.dao.impl.PublicationDAO;
import by.andruhovich.subscription.entity.Author;
import by.andruhovich.subscription.entity.Publication;
import by.andruhovich.subscription.exception.ConnectionTechnicalException;
import by.andruhovich.subscription.exception.DAOTechnicalException;
import by.andruhovich.subscription.exception.ServiceTechnicalException;
import by.andruhovich.subscription.pool.ConnectionFactory;

import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;

/**
 * Provides methods to process author information
 */
public class AuthorService extends BaseService{
    /**
     * @param authorFirstName Author first name
     * @param authorLastName Author last name
     * @param publisherName Publisher name
     * @return Author id in database
     * @throws ServiceTechnicalException
     *          If there was an error during processing operation
     */
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

    /**
     * @param id Author id in database
     * @param authorLastName Author last name
     * @param authorFirstName Author first name
     * @param publisherName Publisher name
     * @return {@code true} if the operation has been completed successfully
     *         {@code false} otherwise
     * @throws ServiceTechnicalException
     *          If there was an error during processing operation
     */
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

    /**
     * @param authorId Author id in database
     * @return {@code true} if the operation has been completed successfully
     *         {@code false} otherwise
     * @throws ServiceTechnicalException
     *          If there was an error during processing operation
     */
    public boolean deleteAuthor(String authorId) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;
        int intAuthorId = Integer.parseInt(authorId);

        try {
            connection = connectionFactory.getConnection();
            PublicationManagerDAO publicationManagerDAO = new PublicationDAO(connection);
            AuthorPublicationManagerDAO authorPublicationManagerDAO = new AuthorPublicationDAO(connection);
            AuthorManagerDAO authorManagerDAO = new AuthorDAO(connection);
            int entityCount = publicationManagerDAO.findPublicationCountByAuthorId(intAuthorId);
            List<Publication> publications = authorPublicationManagerDAO.findPublicationsByAuthorId(intAuthorId, 0, entityCount);
            if (!publications.isEmpty()) {
                PublicationDAO publicationDAO = new PublicationDAO(connection);
                for (Publication publication : publications) {
                    publicationDAO.delete(publication.getPublicationId());
                }
            }
            return authorManagerDAO.delete(intAuthorId);
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    /**
     * @return Author count in database
     * @throws ServiceTechnicalException
     *          If there was an error during processing operation
     */
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

    /**
     * @param pageNumber Current page number from jsp
     * @return Author list from database
     * @throws ServiceTechnicalException
     *          If there was an error during processing operation
     */
    public List<Author> showAuthors(String pageNumber) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;
        int page = Integer.parseInt(pageNumber);

        try {
            connection = connectionFactory.getConnection();
            AuthorManagerDAO authorManagerDAO = new AuthorDAO(connection);
            int startIndex = (page - 1) * ENTITY_COUNT_FOR_ONE_PAGE;
            return authorManagerDAO.findAll(startIndex, ENTITY_COUNT_FOR_ONE_PAGE);
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    /**
     * @param authorFirstNames Author last name
     * @param authorLastNames Author first name
     * @param publisherName Publisher name
     * @return Author list from database
     * @throws ServiceTechnicalException
     *          If there was an error during processing operation
     */
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

    /**
     * @param authorFirstName Author first name
     * @param authorLastName Author last name
     * @param publisherName Publisher name
     * @return Author id in database
     * @throws ServiceTechnicalException
     *          If there was an error during processing operation
     */
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
