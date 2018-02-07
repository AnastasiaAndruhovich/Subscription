package by.andruhovich.subscription.dao;

import by.andruhovich.subscription.entity.PublicationType;
import by.andruhovich.subscription.exception.DAOTechnicalException;

import java.sql.Connection;

public abstract class PublicationTypeManagerDAO extends ManagerDAO<PublicationType> {

    public PublicationTypeManagerDAO(Connection connection) {
        super(connection);
    }

    public abstract int findIdByEntity(PublicationType publicationType) throws DAOTechnicalException;
}
