package by.andruhovich.subscription.service;

import by.andruhovich.subscription.dao.DAOFactory;
import by.andruhovich.subscription.dao.impl.GenreDAO;
import by.andruhovich.subscription.entity.Genre;
import by.andruhovich.subscription.entity.Publication;
import by.andruhovich.subscription.exception.ConnectionTechnicalException;
import by.andruhovich.subscription.exception.DAOTechnicalException;
import by.andruhovich.subscription.exception.ServiceTechnicalException;

import java.util.List;

public class GenreService {
    private static final int ENTITY_QUANTITY_FOR_ONE_PAGE = 10;

    int findIdByGenreName(String genreName) throws ServiceTechnicalException {
        DAOFactory daoFactory = DAOFactory.getInstance();
        GenreDAO genreDAO = null;
        Genre genre = new Genre(genreName);

        try {
            genreDAO = daoFactory.createGenreDAO();
            return genreDAO.findIdByEntity(genre);
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            daoFactory.closeDAO(genreDAO);
        }
    }

    public int addGenre(String name, String description) throws ServiceTechnicalException {
        DAOFactory daoFactory = DAOFactory.getInstance();
        GenreDAO genreDAO = null;
        Genre genre = new Genre(name, description);

        try {
            genreDAO = daoFactory.createGenreDAO();
            return genreDAO.create(genre);
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            daoFactory.closeDAO(genreDAO);
        }
    }

    public List<Genre> showGenres(String pageNumber) throws ServiceTechnicalException {
        DAOFactory daoFactory = DAOFactory.getInstance();
        GenreDAO genreDAO = null;
        int number = Integer.parseInt(pageNumber);
        int startIndex = (number - 1) * ENTITY_QUANTITY_FOR_ONE_PAGE;
        int endIndex = startIndex + ENTITY_QUANTITY_FOR_ONE_PAGE ;

        try {
            genreDAO = daoFactory.createGenreDAO();
            return genreDAO.findAll(startIndex, endIndex);
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            daoFactory.closeDAO(genreDAO);
        }
    }

    public boolean deleteGenre(String genreId) throws ServiceTechnicalException {
        DAOFactory daoFactory = DAOFactory.getInstance();
        GenreDAO genreDAO = null;
        int intGenreId = Integer.parseInt(genreId);

        try {
            genreDAO = daoFactory.createGenreDAO();
            return genreDAO.delete(intGenreId);
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            daoFactory.closeDAO(genreDAO);
        }
    }

    public boolean updateGenre(String genreId, String name, String description) throws ServiceTechnicalException {
        DAOFactory daoFactory = DAOFactory.getInstance();
        GenreDAO genreDAO = null;
        int intGenreId = Integer.parseInt(genreId);
        Genre genre = new Genre(intGenreId, name, description);

        try {
            genreDAO = daoFactory.createGenreDAO();
            return genreDAO.update(genre);
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            daoFactory.closeDAO(genreDAO);
        }
    }
}
