package by.andruhovich.subscription.dao.impl;

import by.andruhovich.subscription.dao.AuthorManagerDAO;
import by.andruhovich.subscription.entity.Author;
import by.andruhovich.subscription.entity.Publication;
import by.andruhovich.subscription.exception.DAOTechnicalException;
import by.andruhovich.subscription.mapper.AuthorMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AuthorDAO extends AuthorManagerDAO {
    private static final String INSERT_AUTHOR = "INSERT INTO authors(publisher_name, author_lastname, author_firstname) " +
            "VALUES (?, ?, ?)";
    private static final String DELETE_AUTHOR_BY_ID = "DELETE FROM authors WHERE author_id = ?";
    private static final String SELECT_AUTHOR_BY_ID = "SELECT * FROM authors WHERE author_id = ?";
    private static final String SELECT_ALL_AUTHORS = "SELECT * FROM authors LIMIT ?, ?";
    private static final String UPDATE_AUTHOR = "UPDATE authors SET publisher_name = ?, author_lastname = ?, " +
            "author_firstname = ? WHERE author_id = ?";

    public AuthorDAO(Connection connection) {
        super(connection);
    }

    @Override
    public int create(Author entity) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;
        AuthorMapper mapper = new AuthorMapper();
        int id = -1;

        try {
            preparedStatement = connection.prepareStatement(INSERT_AUTHOR);
            preparedStatement = mapper.mapEntityToPreparedStatement(preparedStatement, entity);
            preparedStatement.executeQuery();
            preparedStatement = connection.prepareStatement(SELECT_LAST_INSERT_ID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                id = resultSet.getInt("author_id");
            }
            return id;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
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
            throw new DAOTechnicalException("Execute statement error. ", e);
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
            AuthorMapper mapper = new AuthorMapper();
            authors = mapper.mapResultSetToEntity(resultSet);
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
        List<Author> authors;
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SELECT_ALL_AUTHORS);
            preparedStatement.setInt(1, startIndex);
            preparedStatement.setInt(2, endIndex);
            ResultSet resultSet = preparedStatement.executeQuery();
            AuthorMapper mapper = new AuthorMapper();
            authors = mapper.mapResultSetToEntity(resultSet);
            return authors;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public boolean update(Author entity) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(UPDATE_AUTHOR);
            AuthorMapper mapper = new AuthorMapper();
            preparedStatement = mapper.mapEntityToPreparedStatement(preparedStatement, entity);
            preparedStatement.executeQuery();
            return true;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public List<Publication> findPublicationsByAuthorId(int id) throws DAOTechnicalException {
        AuthorPublicationDAO authorPublicationDAO = new AuthorPublicationDAO(connection);
        return authorPublicationDAO.findPublicationsByAuthorId(id);
    }
}
