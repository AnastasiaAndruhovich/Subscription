package by.andruhovich.subscription.dao;

import by.andruhovich.subscription.entity.Author;
import by.andruhovich.subscription.entity.Publication;
import by.andruhovich.subscription.exception.DAOTechnicalException;

import java.sql.Connection;
import java.util.List;

public abstract class AuthorPublicationManagerDAO extends BaseDAO {
    public AuthorPublicationManagerDAO(Connection connection) {
        super(connection);
    }

    public abstract boolean createRecord(Author author, Publication publication) throws DAOTechnicalException;
    public abstract List<Author> findAuthorsByPublicationId(int id) throws DAOTechnicalException;
    public abstract List<Publication> findPublicationsByAuthorId(int id, int startIndex, int endIndex) throws DAOTechnicalException;
    public abstract boolean deletePublicationsByAuthorId(int authorId) throws DAOTechnicalException;
    public abstract boolean deleteRecordByPublicationId(int id) throws DAOTechnicalException;
}
