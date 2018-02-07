package by.andruhovich.subscription.dao.impl;

import by.andruhovich.subscription.dao.AuthorManagerDAO;
import by.andruhovich.subscription.entity.Author;
import by.andruhovich.subscription.exception.DAOTechnicalException;
import by.andruhovich.subscription.mapper.AuthorMapper;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AuthorDAO extends AuthorManagerDAO {
    private static final String INSERT_AUTHOR = "INSERT INTO authors(publisher_name, author_lastname, author_firstname) " +
            "VALUES (?, ?, ?)";
    private static final String SELECT_LAST_INSERT_ID = "SELECT author_id FROM authors ORDER BY author_id DESC LIMIT 1";
    private static final String DELETE_AUTHOR_BY_ID = "DELETE FROM authors WHERE author_id = ?";
    private static final String SELECT_COUNT = "SELECT COUNT(author_id) AS count FROM authors";
    private static final String SELECT_AUTHOR_BY_ID = "SELECT * FROM authors WHERE author_id = ?";
    private static final String SELECT_ALL_AUTHORS = "SELECT * FROM authors ORDER BY author_id DESC LIMIT ?, ?";
    private static final String UPDATE_AUTHOR = "UPDATE authors SET publisher_name = ?, author_lastname = ?, " +
            "author_firstname = ? WHERE author_id = ?";

    private static final String SELECT_AUTHOR_ID_BY_AUTHOR_FIELDS = "SELECT author_id FROM authors " +
            "WHERE publisher_name = ? && author_firstname = ? && author_lastname = ?";

    private static final Logger LOGGER = LogManager.getLogger(AuthorDAO.class);

    public AuthorDAO(Connection connection) {
        super(connection);
    }

    @Override
    public int create(Author entity) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for author create");
        PreparedStatement preparedStatement = null;
        PreparedStatement statement = null;
        AuthorMapper mapper = new AuthorMapper();
        int id = -1;

        try {
            preparedStatement = connection.prepareStatement(INSERT_AUTHOR);
            preparedStatement = mapper.mapEntityToPreparedStatement(preparedStatement, entity);
            preparedStatement.executeUpdate();
            statement = connection.prepareStatement(SELECT_LAST_INSERT_ID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                id = resultSet.getInt("author_id");
            }
            LOGGER.log(Level.INFO, "Request for create author - succeed");
            return id;
        } catch (MySQLIntegrityConstraintViolationException e) {
            LOGGER.log(Level.INFO, "Author is already exist");
            return id;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
            close(statement);
        }
    }

    @Override
    public boolean delete(int authorId) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for author delete");
        return delete(authorId, DELETE_AUTHOR_BY_ID);
    }

    @Override
    public Author findEntityById(int id) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for find entity by id");
        PreparedStatement preparedStatement = null;
        List<Author> authors;

        try {
            preparedStatement = connection.prepareStatement(SELECT_AUTHOR_BY_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            AuthorMapper mapper = new AuthorMapper();
            authors = mapper.mapResultSetToEntity(resultSet);
            LOGGER.log(Level.INFO, "Request for find entity by id - succeed");
            if (authors.isEmpty()) {
                return null;
            }
            return authors.get(0);
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public List<Author> findAll(int startIndex, int endIndex) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for find all");
        List<Author> authors;
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SELECT_ALL_AUTHORS);
            preparedStatement.setInt(1, startIndex);
            preparedStatement.setInt(2, endIndex);
            ResultSet resultSet = preparedStatement.executeQuery();
            AuthorMapper mapper = new AuthorMapper();
            authors = mapper.mapResultSetToEntity(resultSet);
            LOGGER.log(Level.INFO, "Request for find all - succeed");
            return authors;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public boolean update(Author entity) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for update author");
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(UPDATE_AUTHOR);
            AuthorMapper mapper = new AuthorMapper();
            preparedStatement = mapper.mapEntityToPreparedStatement(preparedStatement, entity);
            preparedStatement.setInt(4, entity.getAuthorId());
            preparedStatement.executeUpdate();
            LOGGER.log(Level.INFO, "Request for update author - succeed");
            return true;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public int findIdByEntity(Author author) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for find id by entity");
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SELECT_AUTHOR_ID_BY_AUTHOR_FIELDS);
            preparedStatement.setString(1, author.getPublisherName());
            preparedStatement.setString(2, author.getAuthorFirstName());
            preparedStatement.setString(3, author.getAuthorLastName());
            ResultSet resultSet = preparedStatement.executeQuery();
            int id = -1;
            while (resultSet.next()) {
                id = resultSet.getInt("author_id");
            }
            LOGGER.log(Level.INFO, "Request for find id by entity - succeed");
            return id;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    public int findEntityCount() throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for get count");
        return findEntityCount(SELECT_COUNT);
    }

}
