package by.andruhovich.subscription.dao;

import by.andruhovich.subscription.entity.Author;
import by.andruhovich.subscription.entity.Genre;
import by.andruhovich.subscription.entity.Publication;
import by.andruhovich.subscription.entity.PublicationType;
import by.andruhovich.subscription.exception.DAOTechnicalException;

import java.sql.Connection;
import java.util.List;

public abstract class PublicationManagerDAO extends MediatorManagerDAO<Publication> {
    public PublicationManagerDAO(Connection connection) {
        super(connection);
    }

    public abstract Genre findGenreByPublicationId(int id) throws DAOTechnicalException;
    public abstract PublicationType findPublicationTypeByPublicationId(int id) throws DAOTechnicalException;
    public abstract List<Author> findAuthorsByPublicationId(int id) throws DAOTechnicalException;

    public abstract List<Publication> findPublicationsByGenreId(int id) throws DAOTechnicalException;
    public abstract List<Publication> findPublicationsByPublicationTypeId(int id) throws DAOTechnicalException;
}
