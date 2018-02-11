package by.andruhovich.subscription.dao;

import by.andruhovich.subscription.entity.Author;
import by.andruhovich.subscription.exception.DAOTechnicalException;

import java.sql.Connection;

/**
 * Concrete Manager extends ManagerDAO parametrize by Author entity
 */
public abstract class AuthorManagerDAO extends ManagerDAO<Author> {

    /**
     * @param connection java.sql.Connection to initialize super class
     */
    public AuthorManagerDAO(Connection connection) {
        super(connection);
    }

    /**
     * @param author Author to found in database
     * @return Author id in database
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    public abstract int findIdByEntity(Author author) throws DAOTechnicalException;
}
