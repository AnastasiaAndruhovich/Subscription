package by.andruhovich.subscription.dao;

import by.andruhovich.subscription.entity.Author;
import by.andruhovich.subscription.entity.Publication;
import by.andruhovich.subscription.exception.DAOTechnicalException;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class AuthorPublicationDAO {
    Connection connection;

    public AuthorPublicationDAO(Connection connection) {
        this.connection = connection;
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
            authors = createAuthorList(resultSet);
            return authors;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        } catch (DAOTechnicalException e) {
            throw new DAOTechnicalException(e.getMessage());
        } finally {
            close(preparedStatement);
        }
    }

    private List<Author> createAuthorList(ResultSet resultSet) throws DAOTechnicalException {
        List<Author> authors = new LinkedList<>();
        Author author;
        try {
            while (resultSet.next()) {
                int authorId = resultSet.getInt("author_id");
                String publisherName = resultSet.getString("publisher_name");
                String authorLastname = resultSet.getString("author_firstname");
                String authorFirstname = resultSet.getString("author_lastname");
                author = new Author(authorId, publisherName, authorLastname, authorFirstname);
                authors.add(author);
            }
            return authors;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        }
    }

    public void close(Statement statement) {
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            // TODO log
        }
    }
}
