package by.andruhovich.subscription.service;

import by.andruhovich.subscription.pool.ConnectionFactory;
import by.andruhovich.subscription.dao.impl.GenreDAO;
import by.andruhovich.subscription.entity.Genre;
import by.andruhovich.subscription.exception.ConnectionTechnicalException;
import by.andruhovich.subscription.exception.DAOTechnicalException;
import by.andruhovich.subscription.exception.ServiceTechnicalException;

import java.sql.Connection;
import java.util.List;

public class GenreService extends BaseService {

    int findIdByGenreName(String genreName) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;
        Genre genre = new Genre(genreName);

        try {
            connection = connectionFactory.getConnection();
            GenreDAO genreDAO = new GenreDAO(connection);
            return genreDAO.findIdByEntity(genre);
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    public int addGenre(String name, String description) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;
        Genre genre = new Genre(name, description);

        try {
            connection = connectionFactory.getConnection();
            GenreDAO genreDAO = new GenreDAO(connection);
            return genreDAO.create(genre);
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    public List<Genre> showGenres(String pageNumber) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;
        int number = Integer.parseInt(pageNumber);
        int startIndex = (number - 1) * ENTITY_COUNT_FOR_ONE_PAGE;
        int endIndex = startIndex + ENTITY_COUNT_FOR_ONE_PAGE;

        try {
            connection = connectionFactory.getConnection();
            GenreDAO genreDAO = new GenreDAO(connection);
            return genreDAO.findAll(startIndex, endIndex);
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    public boolean deleteGenre(String genreId) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;
        int intGenreId = Integer.parseInt(genreId);

        try {
            connection = connectionFactory.getConnection();
            GenreDAO genreDAO = new GenreDAO(connection);
            return genreDAO.delete(intGenreId);
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    public boolean updateGenre(String genreId, String name, String description) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;
        int intGenreId = Integer.parseInt(genreId);
        Genre genre = new Genre(intGenreId, name, description);

        try {
            connection = connectionFactory.getConnection();
            GenreDAO genreDAO = new GenreDAO(connection);
            return genreDAO.update(genre);
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

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
