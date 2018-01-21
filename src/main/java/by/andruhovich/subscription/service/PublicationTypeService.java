package by.andruhovich.subscription.service;

import by.andruhovich.subscription.dao.DAOFactory;
import by.andruhovich.subscription.dao.impl.PublicationTypeDAO;
import by.andruhovich.subscription.entity.PublicationType;
import by.andruhovich.subscription.exception.ConnectionTechnicalException;
import by.andruhovich.subscription.exception.DAOTechnicalException;
import by.andruhovich.subscription.exception.ServiceTechnicalException;

import java.util.List;

public class PublicationTypeService {
    private static final int ENTITY_QUANTITY_FOR_ONE_PAGE = 10;

    int findIdByPublicationTypeName(String publicationTypeName) throws ServiceTechnicalException {
        DAOFactory daoFactory = DAOFactory.getInstance();
        PublicationTypeDAO publicationTypeDAO = null;
        PublicationType publicationType = new PublicationType(publicationTypeName);

        try {
            publicationTypeDAO = daoFactory.createPublicationTypeDAO();
            return publicationTypeDAO.findIdByEntity(publicationType);
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            daoFactory.closeDAO(publicationTypeDAO);
        }
    }

    public int addPublicationType(String publicationTypeName) throws ServiceTechnicalException {
        DAOFactory daoFactory = DAOFactory.getInstance();
        PublicationTypeDAO publicationTypeDAO = null;
        PublicationType publicationType = new PublicationType(publicationTypeName);

        try {
            publicationTypeDAO = daoFactory.createPublicationTypeDAO();
            return publicationTypeDAO.create(publicationType);
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            daoFactory.closeDAO(publicationTypeDAO);
        }
    }

    public boolean deletePublicationType(String publicationTypeId) throws ServiceTechnicalException {
        DAOFactory daoFactory = DAOFactory.getInstance();
        PublicationTypeDAO publicationTypeDAO = null;
        int intPublicationTypeId = Integer.parseInt(publicationTypeId);

        try {
            publicationTypeDAO = daoFactory.createPublicationTypeDAO();
            return publicationTypeDAO.delete(intPublicationTypeId);
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            daoFactory.closeDAO(publicationTypeDAO);
        }
    }

    public boolean updatePublicationType(String publicationTypeId, String publicationTypeName)
            throws ServiceTechnicalException {
        DAOFactory daoFactory = DAOFactory.getInstance();
        PublicationTypeDAO publicationTypeDAO = null;
        int intPublicationTypeId = Integer.parseInt(publicationTypeId);
        PublicationType publicationType = new PublicationType(intPublicationTypeId, publicationTypeName);

        try {
            publicationTypeDAO = daoFactory.createPublicationTypeDAO();
            return publicationTypeDAO.update(publicationType);
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            daoFactory.closeDAO(publicationTypeDAO);
        }
    }

    public List<PublicationType> showPublicationTypes(String pageNumber) throws ServiceTechnicalException {
        DAOFactory daoFactory = DAOFactory.getInstance();
        PublicationTypeDAO publicationTypeDAO = null;

        int number = Integer.parseInt(pageNumber);
        int startIndex = (number - 1) * ENTITY_QUANTITY_FOR_ONE_PAGE;
        int endIndex = startIndex + ENTITY_QUANTITY_FOR_ONE_PAGE ;

        try {
            publicationTypeDAO = daoFactory.createPublicationTypeDAO();
            return  publicationTypeDAO.findAll(startIndex, endIndex);
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            daoFactory.closeDAO(publicationTypeDAO);
        }
    }
}
