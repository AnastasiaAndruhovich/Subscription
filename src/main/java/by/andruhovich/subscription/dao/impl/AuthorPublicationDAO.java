package by.andruhovich.subscription.dao.impl;

import by.andruhovich.subscription.dao.BaseDAO;
import by.andruhovich.subscription.entity.Author;
import by.andruhovich.subscription.entity.Publication;
import by.andruhovich.subscription.exception.DAOTechnicalException;
import by.andruhovich.subscription.mapper.AuthorMapper;
import by.andruhovich.subscription.mapper.PublicationMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AuthorPublicationDAO extends BaseDAO {
    private final static String INSERT_RECORD = "INSERT INTO authors_publications(author_id, publication_id) VALUES(?, ?)";
    private final static String SELECT_AUTHOR_BY_PUBLICATION_ID = "SELECT a.author_id, a.publisher_name, a.author_lastname, " +
            "a.author_firstname FROM authors_publications RIGHT JOIN authors a USING (author_id) WHERE publication_id = ?";
    private final static String SELECT_PUBLICATION_BY_AUTHOR_ID = "SELECT p.publication_id, p.name, p.description, " +
            "p.price, p.picture_name, p.picture FROM authors_publications RIGHT JOIN publications p " +
            "USING (publication_id) WHERE author_id = ?";

    public AuthorPublicationDAO(Connection connection) {
        super(connection);
    }

    public boolean createRecord(Author author, Publication publication) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(INSERT_RECORD);
            preparedStatement.setInt(1, author.getAuthorId());
            preparedStatement.setInt(2, publication.getPublicationId());
            preparedStatement.executeQuery();
            return true;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        } finally {
            close(preparedStatement);
        }
    }

    public List<Author> findAuthorByPublicationId(int id) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;
        List<Author> authors;

        try {
            preparedStatement = connection.prepareStatement(SELECT_AUTHOR_BY_PUBLICATION_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            AuthorMapper authorMapper = new AuthorMapper();
            authors = authorMapper.mapResultSetToEntity(resultSet);
            return authors;
        } catch (SQLException | DAOTechnicalException e) {
            throw new DAOTechnicalException(e.getMessage());
        } finally {
            close(preparedStatement);
        }
    }

    public List<Publication> findPublicationsByAuthorId(int id) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;
        List<Publication> publications;

        try {
            preparedStatement = connection.prepareStatement(SELECT_PUBLICATION_BY_AUTHOR_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            PublicationMapper publicationMapper = new PublicationMapper();
            publications = publicationMapper.mapResultSetToEntity(resultSet);
            return publications;
        } catch (SQLException | DAOTechnicalException e) {
            throw new DAOTechnicalException(e.getMessage());
        } finally {
            close(preparedStatement);
        }
    }

}
