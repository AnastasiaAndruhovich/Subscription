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

public class GenreService extends BaseService {

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

    public List<Genre> showGenres(String pageNumber) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;

        try {
            connection = connectionFactory.getConnection();
            GenreManagerDAO genreManagerDAO = new GenreDAO(connection);
            int entityCount = genreManagerDAO.findEntityCount();
            int startIndex = findStartIndex(pageNumber, entityCount);
            int endIndex = startIndex + ENTITY_COUNT_FOR_ONE_PAGE;
            return genreManagerDAO.findAll(startIndex, endIndex);
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
            GenreManagerDAO genreManagerDAO = new GenreDAO(connection);
            return genreManagerDAO.delete(intGenreId);
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
            GenreManagerDAO genreManagerDAO = new GenreDAO(connection);
            return genreManagerDAO.update(genre);
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
