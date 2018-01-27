package by.andruhovich.subscription.service;

import by.andruhovich.subscription.dao.DAOFactory;
import by.andruhovich.subscription.dao.impl.PublicationDAO;
import by.andruhovich.subscription.dao.impl.PublicationTypeDAO;
import by.andruhovich.subscription.entity.Publication;
import by.andruhovich.subscription.entity.PublicationType;
import by.andruhovich.subscription.exception.ConnectionTechnicalException;
import by.andruhovich.subscription.exception.DAOTechnicalException;
import by.andruhovich.subscription.exception.ServiceTechnicalException;

import java.util.List;

public class PublicationTypeService extends BaseService{
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
        int startIndex = (number - 1) * ENTITY_COUNT_FOR_ONE_PAGE;
        int endIndex = startIndex + ENTITY_COUNT_FOR_ONE_PAGE;

        try {
            publicationTypeDAO = daoFactory.createPublicationTypeDAO();
            return  publicationTypeDAO.findAll(startIndex, endIndex);
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            daoFactory.closeDAO(publicationTypeDAO);
        }
    }

    public List<Publication> findPublicationByPublicationType(String publicationTypeId) throws ServiceTechnicalException {
        DAOFactory daoFactory = DAOFactory.getInstance();
        PublicationDAO publicationDAO = null;
        int intPublicationTypeId = Integer.parseInt(publicationTypeId);

        try {
            publicationDAO = daoFactory.createPublicationDAO();
            return publicationDAO.findPublicationsByPublicationTypeId(intPublicationTypeId);
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            daoFactory.closeDAO(publicationDAO);
        }
    }

    public int findPublicationTypePageCount() throws ServiceTechnicalException {
        DAOFactory daoFactory = DAOFactory.getInstance();
        PublicationTypeDAO publicationTypeDAO = null;

        try {
            publicationTypeDAO = daoFactory.createPublicationTypeDAO();
            int count = publicationTypeDAO.findEntityCount();
            return (int)Math.ceil((double)(count) / ENTITY_COUNT_FOR_ONE_PAGE);
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            daoFactory.closeDAO(publicationTypeDAO);
        }
    }
}
