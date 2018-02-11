package by.andruhovich.subscription.service;

import by.andruhovich.subscription.dao.PublicationTypeManagerDAO;
import by.andruhovich.subscription.dao.impl.PublicationTypeDAO;
import by.andruhovich.subscription.entity.PublicationType;
import by.andruhovich.subscription.exception.ConnectionTechnicalException;
import by.andruhovich.subscription.exception.DAOTechnicalException;
import by.andruhovich.subscription.exception.ServiceTechnicalException;
import by.andruhovich.subscription.pool.ConnectionFactory;

import java.sql.Connection;
import java.util.List;

/**
 * Provides methods to process publication type information
 */
public class PublicationTypeService extends BaseService {
    /**
     * @param publicationTypeName Publication type name
     * @return Relevant id in database
     * @throws ServiceTechnicalException
     *          If there was an error during processing operation
     */
    public int findIdByPublicationTypeName(String publicationTypeName) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;
        PublicationType publicationType = new PublicationType(publicationTypeName);

        try {
            connection = connectionFactory.getConnection();
            PublicationTypeManagerDAO publicationTypeManagerDAO = new PublicationTypeDAO(connection);
            return publicationTypeManagerDAO.findIdByEntity(publicationType);
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    /**
     * @param publicationTypeName Publication type name
     * @return Created publication type id in database
     * @throws ServiceTechnicalException
     *          If there was an error during processing operation
     */
    public int addPublicationType(String publicationTypeName) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;
        PublicationType publicationType = new PublicationType(publicationTypeName);

        try {
            connection = connectionFactory.getConnection();
            PublicationTypeManagerDAO publicationTypeManagerDAO = new PublicationTypeDAO(connection);
            return publicationTypeManagerDAO.create(publicationType);
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    /**
     * @param publicationTypeId Publication type id
     * @return {@code true} if the operation has been completed successfully
     *         {@code false} otherwise
     * @throws ServiceTechnicalException
     *          If there was an error during processing operation
     */
    public boolean deletePublicationType(String publicationTypeId) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;
        int intPublicationTypeId = Integer.parseInt(publicationTypeId);

        try {
            connection = connectionFactory.getConnection();
            PublicationTypeManagerDAO publicationTypeManagerDAO = new PublicationTypeDAO(connection);
            return publicationTypeManagerDAO.delete(intPublicationTypeId);
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    /**
     * @param publicationTypeId Publication type id
     * @param publicationTypeName Publication type name
     * @return {@code true} if the operation has been completed successfully
     *         {@code false} otherwise
     * @throws ServiceTechnicalException
     *          If there was an error during processing operation
     */
    public boolean updatePublicationType(String publicationTypeId, String publicationTypeName)
            throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;
        int intPublicationTypeId = Integer.parseInt(publicationTypeId);
        PublicationType publicationType = new PublicationType(intPublicationTypeId, publicationTypeName);

        try {
            connection = connectionFactory.getConnection();
            PublicationTypeManagerDAO publicationTypeManagerDAO = new PublicationTypeDAO(connection);
            return publicationTypeManagerDAO.update(publicationType);
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    /**
     * @param pageNumber Current page number from jsp
     * @return Publication type list from database
     * @throws ServiceTechnicalException
     *          If there was an error during processing operation
     */
    public List<PublicationType> showPublicationTypes(String pageNumber) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;
        int page = Integer.parseInt(pageNumber);

        try {
            connection = connectionFactory.getConnection();
            PublicationTypeManagerDAO publicationTypeManagerDAO = new PublicationTypeDAO(connection);
            int startIndex = (page - 1) * ENTITY_COUNT_FOR_ONE_PAGE;
            return  publicationTypeManagerDAO.findAll(startIndex, ENTITY_COUNT_FOR_ONE_PAGE);
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    /**
     * @return Publication type count from database
     * @throws ServiceTechnicalException
     *          If there was an error during processing operation
     */
    public int findPublicationTypePageCount() throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;

        try {
            connection = connectionFactory.getConnection();
            PublicationTypeDAO publicationTypeDAO = new PublicationTypeDAO(connection);
            int count = publicationTypeDAO.findEntityCount();
            return (int)Math.ceil((double)(count) / ENTITY_COUNT_FOR_ONE_PAGE);
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }
}
