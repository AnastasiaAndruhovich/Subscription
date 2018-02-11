package by.andruhovich.subscription.dao;

import by.andruhovich.subscription.entity.Author;
import by.andruhovich.subscription.entity.Publication;
import by.andruhovich.subscription.exception.DAOTechnicalException;

import java.sql.Connection;
import java.util.List;

/**
 * Concrete Manager extends BaseDAO
 */
public abstract class AuthorPublicationManagerDAO extends BaseDAO {

    /**
     * @param connection java.sql.Connection to initialize super class
     */
    public AuthorPublicationManagerDAO(Connection connection) {
        super(connection);
    }

    /**
     * @param author Author entity to be added in database
     * @param publication Publication entity to be added in database
     * @return {@code true} if the operation has been completed successfully
     *         {@code false} otherwise
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    public abstract boolean createRecord(Author author, Publication publication) throws DAOTechnicalException;

    /**
     * @param id Publication id in database
     * @return Author list relevant to publication id in database
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    public abstract List<Author> findAuthorsByPublicationId(int id) throws DAOTechnicalException;

    /**
     * @param id Author id in database
     * @param startIndex Relevant to author id publication start index in database
     * @param endIndex Relevant to author id publication end index in database
     * @return Publication list relevant to author id in database
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    public abstract List<Publication> findPublicationsByAuthorId(int id, int startIndex, int endIndex) throws DAOTechnicalException;

    /**
     * @param id Publication id in database
     * @return {@code true} if the operation has been completed successfully
     *         {@code false} otherwise
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    public abstract boolean deleteRecordByPublicationId(int id) throws DAOTechnicalException;
}
