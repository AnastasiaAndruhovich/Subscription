package by.andruhovich.subscription.dao;

import by.andruhovich.subscription.entity.PublicationType;
import by.andruhovich.subscription.exception.DAOTechnicalException;

import java.sql.Connection;

/**
 * Concrete Manager extends ManagerDAO parametrize by PublicationType entity
 */
public abstract class PublicationTypeManagerDAO extends ManagerDAO<PublicationType> {

    /**
     * @param connection java.sql.Connection to initialize super class
     */
    public PublicationTypeManagerDAO(Connection connection) {
        super(connection);
    }

    /**
     * @param publicationType Publication type
     * @return Id relevant to publication type in database
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    public abstract int findIdByEntity(PublicationType publicationType) throws DAOTechnicalException;
}
