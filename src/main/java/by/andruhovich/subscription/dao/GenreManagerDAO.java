package by.andruhovich.subscription.dao;

import by.andruhovich.subscription.entity.Genre;
import by.andruhovich.subscription.exception.DAOTechnicalException;

import java.sql.Connection;

/**
 * Concrete Manager extends ManagerDAO parametrize by Genre entity
 */
public abstract class GenreManagerDAO extends ManagerDAO<Genre> {

    /**
     * @param connection java.sql.Connection to initialize super class
     */
    public GenreManagerDAO(Connection connection) {
        super(connection);
    }

    /**
     * @param genre Genre to be found in database
     * @return Genre id in database
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    public abstract int findIdByEntity(Genre genre) throws DAOTechnicalException;
}
