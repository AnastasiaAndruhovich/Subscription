package by.andruhovich.subscription.dao.impl;

import by.andruhovich.subscription.dao.AuthorPublicationManagerDAO;
import by.andruhovich.subscription.entity.Author;
import by.andruhovich.subscription.entity.Publication;
import by.andruhovich.subscription.exception.DAOTechnicalException;
import by.andruhovich.subscription.mapper.AuthorMapper;
import by.andruhovich.subscription.mapper.PublicationMapper;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Concrete DAO extends AuthorPublicationManagerDAO
 */
public class AuthorPublicationDAO extends AuthorPublicationManagerDAO {
    private final static String INSERT_RECORD = "INSERT INTO authors_publications(author_id, publication_id) VALUES(?, ?)";
    private final static String SELECT_AUTHOR_BY_PUBLICATION_ID = "SELECT a.author_id, a.publisher_name, a.author_lastname, " +
            "a.author_firstname FROM authors_publications RIGHT JOIN authors a USING (author_id) WHERE publication_id = ?";
    private final static String SELECT_PUBLICATION_BY_AUTHOR_ID = "SELECT p.publication_id, p.name, p.description, " +
            "p.price, p.picture_name, p.picture FROM authors_publications RIGHT JOIN publications p " +
            "USING (publication_id) WHERE author_id = ? ORDER BY author_id DESC LIMIT ?, ?";
    private final static String DELETE_RECORD_BY_PUBLICATION_ID = "DELETE FROM authors_publications WHERE publication_id = ?";

    private static final Logger LOGGER = LogManager.getLogger(AuthorPublicationDAO.class);

    /**
     * @param connection java.sql.Connection to initialize super class
     */
    public AuthorPublicationDAO(Connection connection) {
        super(connection);
    }

    /**
     * @param author Author entity to be added in database
     * @param publication Publication entity to be added in database
     * @return {@code true} if the operation has been completed successfully
     *         {@code false} otherwise
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    @Override
    public boolean createRecord(Author author, Publication publication) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request fo create record");
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(INSERT_RECORD);
            preparedStatement.setInt(1, author.getAuthorId());
            preparedStatement.setInt(2, publication.getPublicationId());
            preparedStatement.executeUpdate();
            LOGGER.log(Level.INFO, "Request for create record - succeed");
            return true;
        } catch (MySQLIntegrityConstraintViolationException e) {
            LOGGER.log(Level.INFO, "Record is already exist");
            return false;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    /**
     * @param id Publication id in database
     * @return Author list relevant to publication id in database
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    @Override
    public List<Author> findAuthorsByPublicationId(int id) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for find author by publication id");
        PreparedStatement preparedStatement = null;
        List<Author> authors;

        try {
            preparedStatement = connection.prepareStatement(SELECT_AUTHOR_BY_PUBLICATION_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            AuthorMapper authorMapper = new AuthorMapper();
            authors = authorMapper.mapResultSetToEntity(resultSet);
            LOGGER.log(Level.INFO, "Request for find author by publication id - succeed");
            return authors;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    /**
     * @param id Author id in database
     * @param startIndex Relevant to author id publication start index in database
     * @param endIndex Relevant to author id publication end index in database
     * @return Publication list relevant to author id in database
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    @Override
    public List<Publication> findPublicationsByAuthorId(int id, int startIndex, int endIndex) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for find publications by author id");
        PreparedStatement preparedStatement = null;
        List<Publication> publications;

        try {
            preparedStatement = connection.prepareStatement(SELECT_PUBLICATION_BY_AUTHOR_ID);
            preparedStatement.setInt(1, id);
            preparedStatement.setInt(2, startIndex);
            preparedStatement.setInt(3, endIndex);
            ResultSet resultSet = preparedStatement.executeQuery();
            PublicationMapper publicationMapper = new PublicationMapper();
            publications = publicationMapper.mapResultSetToEntity(resultSet);
            LOGGER.log(Level.INFO, "Request for find publications by author id - succeed");
            return publications;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    /**
     * @param id Publication id in database
     * @return {@code true} if the operation has been completed successfully
     *         {@code false} otherwise
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    @Override
    public boolean deleteRecordByPublicationId(int id) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for delete record by publication id");
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(DELETE_RECORD_BY_PUBLICATION_ID);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            LOGGER.log(Level.INFO, "Request for delete record by publication id - succeed");
            return true;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

}
