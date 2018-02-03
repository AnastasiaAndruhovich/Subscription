package by.andruhovich.subscription.dao.impl;

import by.andruhovich.subscription.dao.GenreManagerDAO;
import by.andruhovich.subscription.entity.Genre;
import by.andruhovich.subscription.entity.Publication;
import by.andruhovich.subscription.exception.DAOTechnicalException;
import by.andruhovich.subscription.mapper.GenreMapper;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class GenreDAO extends GenreManagerDAO {
    private static final String INSERT_GENRE= "INSERT INTO genres(name, description) VALUES (?, ?)";
    private static final String SELECT_LAST_INSERT_ID = "SELECT genre_id FROM genres ORDER BY genre_id DESC LIMIT 1";
    private static final String DELETE_GENRE_BY_ID = "DELETE FROM genres WHERE genre_id = ?";
    private static final String SELECT_COUNT = "SELECT COUNT(genre_id) AS count FROM genres";
    private static final String SELECT_GENRE_BY_ID = "SELECT * FROM genres WHERE genre_id = ?";
    private static final String SELECT_ALL_GENRES = "SELECT * FROM genres LIMIT ?, ?";
    private static final String UPDATE_GENRE = "UPDATE genres SET name = ?, description = ? WHERE genre_id = ?";

    private static final String SELECT_GENRE_ID_BY_GENRE_FIELDS = "SELECT genre_id FROM genres WHERE name = ?";

    private static final Logger LOGGER = LogManager.getLogger(GenreDAO.class);

    public GenreDAO(Connection connection) {
        super(connection);
    }

    @Override
    public int create(Genre entity) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for create genre");
        PreparedStatement preparedStatement = null;
        PreparedStatement statement = null;
        GenreMapper mapper = new GenreMapper();
        int id = -1;

        try {
            preparedStatement = connection.prepareStatement(INSERT_GENRE);
            preparedStatement = mapper.mapEntityToPreparedStatement(preparedStatement, entity);
            preparedStatement.executeUpdate();
            statement = connection.prepareStatement(SELECT_LAST_INSERT_ID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                id = resultSet.getInt("genre_id");
            }
            LOGGER.log(Level.INFO, "Request for create genre - succeed");
            return id;
        } catch (MySQLIntegrityConstraintViolationException e) {
            LOGGER.log(Level.INFO, "Genre is already exist");
            return id;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
            close(statement);
        }
    }

    @Override
    public boolean delete(int id) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for delete genre");
        return delete(id, DELETE_GENRE_BY_ID);
    }

    @Override
    public Genre findEntityById(int id) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for find entity by id");
        PreparedStatement preparedStatement = null;
        List<Genre> genres;

        try {
            preparedStatement = connection.prepareStatement(SELECT_GENRE_BY_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            GenreMapper mapper = new GenreMapper();
            genres = mapper.mapResultSetToEntity(resultSet);
            LOGGER.log(Level.INFO, "Request for find entity by id - succeed");
            if (genres.isEmpty()) {
                return null;
            }
            return genres.get(0);
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public List<Genre> findAll(int startIndex, int endIndex) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for find all");
        List<Genre> genres;
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SELECT_ALL_GENRES);
            preparedStatement.setInt(1, startIndex);
            preparedStatement.setInt(2, endIndex);
            ResultSet resultSet = preparedStatement.executeQuery();
            GenreMapper mapper = new GenreMapper();
            genres = mapper.mapResultSetToEntity(resultSet);
            LOGGER.log(Level.INFO, "Request for find all - succeed");
            return genres;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public boolean update(Genre entity) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for update genre");
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(UPDATE_GENRE);
            GenreMapper mapper = new GenreMapper();
            preparedStatement = mapper.mapEntityToPreparedStatement(preparedStatement, entity);
            preparedStatement.setInt(3, entity.getGenreId());
            preparedStatement.executeUpdate();
            LOGGER.log(Level.INFO, "Request for update genre - succeed");
            return true;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public int findIdByEntity(Genre genre) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for find id by entity");
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SELECT_GENRE_ID_BY_GENRE_FIELDS);
            preparedStatement.setString(1, genre.getName());
            ResultSet resultSet = preparedStatement.executeQuery();
            int id = -1;
            while (resultSet.next()) {
                id = resultSet.getInt("genre_id");
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
