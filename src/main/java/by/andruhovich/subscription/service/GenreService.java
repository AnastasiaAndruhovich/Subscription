package by.andruhovich.subscription.service;

import by.andruhovich.subscription.dao.GenreManagerDAO;
import by.andruhovich.subscription.dao.impl.GenreDAO;
import by.andruhovich.subscription.entity.Genre;
import by.andruhovich.subscription.exception.ConnectionTechnicalException;
import by.andruhovich.subscription.exception.DAOTechnicalException;
import by.andruhovich.subscription.exception.ServiceTechnicalException;
import by.andruhovich.subscription.pool.ConnectionFactory;

import java.sql.Connection;
import java.util.List;

/**
 * Provides methods to process genre information
 */
public class GenreService extends BaseService {
    /**
     * @param genreName Genre name to found
     * @return Genre id in database
     * @throws ServiceTechnicalException
     *          If there was an error during processing operation
     */
    int findIdByGenreName(String genreName) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;
        Genre genre = new Genre(genreName);

        try {
            connection = connectionFactory.getConnection();
            GenreManagerDAO genreManagerDAO = new GenreDAO(connection);
            return genreManagerDAO.findIdByEntity(genre);
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    /**
     * @param name Genre name
     * @param description Genre description
     * @return Genre id in database
     * @throws ServiceTechnicalException
     *          If there was an error during processing operation
     */
    public int addGenre(String name, String description) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;
        Genre genre = new Genre(name, description);

        try {
            connection = connectionFactory.getConnection();
            GenreManagerDAO genreManagerDAO = new GenreDAO(connection);
            return genreManagerDAO.create(genre);
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    /**
     * @param pageNumber Current page number from jsp
     * @return Genre list from database
     * @throws ServiceTechnicalException
     *          If there was an error during processing operation
     */
    public List<Genre> showGenres(String pageNumber) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;
        int page = Integer.parseInt(pageNumber);

        try {
            connection = connectionFactory.getConnection();
            GenreManagerDAO genreManagerDAO = new GenreDAO(connection);
            int startIndex = (page - 1) * ENTITY_COUNT_FOR_ONE_PAGE;
            return genreManagerDAO.findAll(startIndex, ENTITY_COUNT_FOR_ONE_PAGE);
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    /**
     * @param genreId Genre id in database
     * @return {@code true} if the operation has been completed successfully
     *         {@code false} otherwise
     * @throws ServiceTechnicalException
     *          If there was an error during processing operation
     */
    public boolean deleteGenre(String genreId) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;
        int intGenreId = Integer.parseInt(genreId);

        try {
            connection = connectionFactory.getConnection();
            GenreManagerDAO genreManagerDAO = new GenreDAO(connection);
            return genreManagerDAO.delete(intGenreId);
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    /**
     * @param genreId Genre id in database
     * @param name Genre name
     * @param description Genre description
     * @return {@code true} if the operation has been completed successfully
     *         {@code false} otherwise
     * @throws ServiceTechnicalException
     *          If there was an error during processing operation
     */
    public boolean updateGenre(String genreId, String name, String description) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;
        int intGenreId = Integer.parseInt(genreId);
        Genre genre = new Genre(intGenreId, name, description);

        try {
            connection = connectionFactory.getConnection();
            GenreManagerDAO genreManagerDAO = new GenreDAO(connection);
            return genreManagerDAO.update(genre);
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    /**
     * @return Genre count in database
     * @throws ServiceTechnicalException
     *          If there was an error during processing operation
     */
    public int findGenrePageCount() throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;

        try {
            connection = connectionFactory.getConnection();
            GenreDAO genreDAO = new GenreDAO(connection);
            int count = genreDAO.findEntityCount();
            return (int)Math.ceil((double)(count) / ENTITY_COUNT_FOR_ONE_PAGE);
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }
}
