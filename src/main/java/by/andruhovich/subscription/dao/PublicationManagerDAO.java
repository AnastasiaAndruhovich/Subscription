package by.andruhovich.subscription.dao;

import by.andruhovich.subscription.entity.Publication;
import by.andruhovich.subscription.exception.DAOTechnicalException;

import java.sql.Connection;

public abstract class PublicationManagerDAO extends MediatorManagerDAO<Publication> {
    public PublicationManagerDAO(Connection connection) {
        super(connection);
    }

    public abstract int findGenreIdByPublicationId(int id) throws DAOTechnicalException;

    public abstract int findPublicationTypeIdByPublicationId(int id) throws DAOTechnicalException;
}
