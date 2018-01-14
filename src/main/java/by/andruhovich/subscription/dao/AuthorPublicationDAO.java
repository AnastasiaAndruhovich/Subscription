package by.andruhovich.subscription.dao;

import by.andruhovich.subscription.entity.Author;
import by.andruhovich.subscription.entity.Publication;
import by.andruhovich.subscription.exception.DAOTechnicalException;
import by.andruhovich.subscription.mapper.AuthorMapper;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class AuthorPublicationDAO extends BaseDAO {
    public AuthorPublicationDAO(Connection connection) {
        super(connection);
    }

    final static String INSERT_RECORD = "INSERT INTO(author_id, publication_id) VALUES(?, ?)";
    final static String SELECT_AUTHOR_BY_PUBLICATION_ID = "SELECT a.publisher_name, a.author_lastname, a.author_firstname" +
            "FROM authors_publications " +
            "RIGHT JOIN authors a USING (author_id) WHERE publication_id = ?";

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
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        } catch (DAOTechnicalException e) {
            throw new DAOTechnicalException(e.getMessage());
        } finally {
            close(preparedStatement);
        }
    }

}
