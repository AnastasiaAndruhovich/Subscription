package by.andruhovich.subscription.service;

import by.andruhovich.subscription.dao.DAOFactory;
import by.andruhovich.subscription.dao.impl.PublicationTypeDAO;
import by.andruhovich.subscription.entity.PublicationType;
import by.andruhovich.subscription.exception.ConnectionTechnicalException;
import by.andruhovich.subscription.exception.DAOTechnicalException;
import by.andruhovich.subscription.exception.ServiceTechnicalException;

public class PublicationTypeService {
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

    public int AddPublicationType(String publicationTypeName) throws ServiceTechnicalException {
        return 0;
    }

    PublicationType findPublicationTypeByPublicationTypeId(int id) throws ServiceTechnicalException {
        return null;
    }
}
