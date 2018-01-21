package by.andruhovich.subscription.dao;

import by.andruhovich.subscription.entity.Publication;
import by.andruhovich.subscription.entity.PublicationType;
import by.andruhovich.subscription.exception.DAOTechnicalException;

import java.sql.Connection;
import java.util.List;

public abstract class PublicationTypeManagerDAO extends MediatorManagerDAO<PublicationType> {

    public PublicationTypeManagerDAO(Connection connection) {
        super(connection);
    }

    public abstract List<Publication> findPublicationsByPublicationTypeId(int id) throws DAOTechnicalException;
    public abstract int findIdByEntity(PublicationType publicationType) throws DAOTechnicalException;
}
