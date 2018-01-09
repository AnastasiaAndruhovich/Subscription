package by.andruhovich.subscription.dao;

import by.andruhovich.subscription.entity.Author;
import by.andruhovich.subscription.exception.DAOTechnicalException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class AuthorDAO extends AuthorManagerDAO {
    private static final String INSERT_AUTHOR = "INSERT INTO authors(publisher_name, author_lastname, author_firstname) " +
            "VALUES (?, ?, ?)";
    private static final String SELECT_LAST_INSERT_ID = "SELECT LAST_INSERT_ID()";
    private static final String DELETE_AUTHOR_BY_ID = "DELETE FROM authors WHERE author_id = ?";
    private static final String SELECT_AUTHOR_BY_ID = "SELECT * FROM authors WHERE author_id = ?";
    private static final String SELECT_ALL_AUTHORS = "SELECT * FROM authors";
    private static final String UPDATE_AUTHOR = "UPDATE authors SET publisher_name = ?, author_lastname = ?, " +
            "author_firstname = ? WHERE author_id = ?";

    public AuthorDAO(Connection connection) {
        super(connection);
    }

    @Override
    public int create(Author entity) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(INSERT_AUTHOR);
            preparedStatement = fillOutStatementByAuthor(preparedStatement, entity);
            preparedStatement.executeQuery();
            preparedStatement = connection.prepareStatement(SELECT_LAST_INSERT_ID);
            ResultSet resultSet = preparedStatement.executeQuery();
            int id = resultSet.getInt("author_id");
            return id;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public boolean delete(Author entity) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(DELETE_AUTHOR_BY_ID);
            preparedStatement.setInt(1, entity.getAuthorId());
            preparedStatement.executeQuery();
            return true;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public Author findEntityById(int id) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;
        List<Author> authors;

        try {
            preparedStatement = connection.prepareStatement(SELECT_AUTHOR_BY_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            authors = createAuthorList(resultSet);
            return authors.get(0);
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public List<Author> findAll() throws DAOTechnicalException {
        List<Author> authors;
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SELECT_ALL_AUTHORS);
            ResultSet resultSet = preparedStatement.executeQuery();
            authors = createAuthorList(resultSet);
            return authors;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public boolean update(Author entity) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(UPDATE_AUTHOR);
            preparedStatement = fillOutStatementByAuthor(preparedStatement, entity);
            preparedStatement.executeQuery();
            return true;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        } finally {
            close(preparedStatement);
        }
    }

    private PreparedStatement fillOutStatementByAuthor(PreparedStatement preparedStatement, Author author)
            throws DAOTechnicalException {
        try {
            preparedStatement.setString(1, author.getPublisherName());
            preparedStatement.setString(2, author.getAuthorLastname());
            preparedStatement.setString(3, author.getAuthorFirstname());
            preparedStatement.setInt(4, author.getAuthorId());
            return preparedStatement;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
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
}
