package by.andruhovich.subscription.service;

import by.andruhovich.subscription.dao.DAOFactory;
import by.andruhovich.subscription.dao.impl.GenreDAO;
import by.andruhovich.subscription.entity.Genre;
import by.andruhovich.subscription.exception.ConnectionTechnicalException;
import by.andruhovich.subscription.exception.DAOTechnicalException;
import by.andruhovich.subscription.exception.ServiceTechnicalException;

public class GenreService {

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

    public int addGenre(String genre) throws ServiceTechnicalException {
        return 0;
    }

    public int addGenre(String genre, String description) throws ServiceTechnicalException {
        return 0;
    }

   Genre  findGenreByGenreId(int genreId) throws ServiceTechnicalException {
        return null;
    }
}
