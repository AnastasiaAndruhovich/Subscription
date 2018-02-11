package by.andruhovich.subscription.dao;

import by.andruhovich.subscription.entity.Genre;
import by.andruhovich.subscription.entity.Publication;
import by.andruhovich.subscription.entity.PublicationType;
import by.andruhovich.subscription.exception.DAOTechnicalException;

import java.sql.Connection;
import java.util.List;

/**
 * Concrete Manager extends ManagerDAO parametrize by Publication entity
 */
public abstract class PublicationManagerDAO extends ManagerDAO<Publication> {
    /**
     * @param connection java.sql.Connection to initialize super class
     */
    public PublicationManagerDAO(Connection connection) {
        super(connection);
    }

    /**
     * @param id Publication id in database
     * @return Genre relevant to publication in database
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    public abstract Genre findGenreByPublicationId(int id) throws DAOTechnicalException;

    /**
     * @param id Publication id in database
     * @return Publication type relevant to database
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    public abstract PublicationType findPublicationTypeByPublicationId(int id) throws DAOTechnicalException;

    /**
     * @return Publication count in database
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    public abstract int findEntityCount() throws DAOTechnicalException;

    /**
     * @param publicationId Publication id in database
     * @return Picture relevant to publication in database
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    public abstract byte[] findPictureByPublicationId(int publicationId) throws DAOTechnicalException;

    /**
     * @param publicationId Publication id in database
     * @return Picture name relevant to publication in database
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    public abstract String findPictureNameByPublicationId(int publicationId) throws DAOTechnicalException;

    /**
     * @param publicationId Publication id in database
     * @param picture Picture in bytes
     * @param pictureName String picture name
     * @return {@code true} if the operation has been completed successfully
     *         {@code false} otherwise
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    public abstract boolean insertImage(int publicationId, byte[] picture, String pictureName) throws DAOTechnicalException;

    /**
     * @param authorId Author id in database
     * @return Publication count relevant to author in database
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    public abstract int findPublicationCountByAuthorId(int authorId) throws DAOTechnicalException;

    /**
     * @param genreId Genre id in database
     * @return Publication count relevant to genre in database
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    public abstract int findPublicationCountByGenreId(int genreId) throws DAOTechnicalException;

    /**
     * @param publicationTypeId Publication type id in database
     * @return Publication count relevant to publicatino type in databases
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    public abstract int findPublicationCountByPublicationTypeId(int publicationTypeId) throws DAOTechnicalException;

    /**
     * @param id Subscription id in database
     * @return Publication relevant to subscription in database
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    public abstract Publication findPublicationBySubscriptionId(int id) throws DAOTechnicalException;

    /**
     * @param publication Publication in database
     * @return Relevant id in database
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    public abstract int findIdByEntity(Publication publication) throws DAOTechnicalException;

    /**
     * @param id Genre id in database
     * @param startIndex Publication start index in database
     * @param endIndex Publication end index in database
     * @return Publication list relevant to genre in database
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    public abstract List<Publication> findPublicationsByGenreId(int id, int startIndex, int endIndex) throws DAOTechnicalException;

    /**
     * @param id Publication type id in database
     * @param startIndex Publication start index in database
     * @param endIndex Publication end index in database
     * @return Publication list relevant to publication type in database
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    public abstract List<Publication> findPublicationsByPublicationTypeId(int id, int startIndex, int endIndex) throws DAOTechnicalException;
}
