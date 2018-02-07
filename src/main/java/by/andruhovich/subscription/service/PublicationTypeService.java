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

public class PublicationTypeService extends BaseService {
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

    public List<PublicationType> showPublicationTypes(String pageNumber) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;

        int number = Integer.parseInt(pageNumber);
        int startIndex = (number - 1) * ENTITY_COUNT_FOR_ONE_PAGE;
        int endIndex = startIndex + ENTITY_COUNT_FOR_ONE_PAGE;

        try {
            connection = connectionFactory.getConnection();
            PublicationTypeManagerDAO publicationTypeManagerDAO = new PublicationTypeDAO(connection);
            return  publicationTypeManagerDAO.findAll(startIndex, endIndex);
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

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
