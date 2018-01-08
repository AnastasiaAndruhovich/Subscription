package by.andruhovich.subscription.dao;

import by.andruhovich.subscription.entity.Genre;
import by.andruhovich.subscription.exception.DAOTechnicalException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class GenreDAO extends BaseDAO<Genre> {
    private static final String INSERT_GENRE= "INSERT INTO accounts(name, description) VALUES (?, ?)";
    private static final String SELECT_LAST_INSERT_ID = "SELECT LAST_INSERT_ID()";
    private static final String DELETE_GENRE_BY_ID = "DELETE genres WHERE genre_id = ?";
    private static final String SELECT_GENRE_BY_ID = "SELECT * FROM genres WHERE genre_id = ?";
    private static final String SELECT_ALL_GENRES = "SELECT * FROM genres";
    private static final String UPDATE_GENRE = "UPDATE genres SET name = ?, description = ? WHERE genre_id = ?";

    @Override
    public int create(Genre entity) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(INSERT_GENRE);
            preparedStatement = fillOutStatementByGenre(preparedStatement, entity);
            preparedStatement.executeQuery();
            preparedStatement = connection.prepareStatement(SELECT_LAST_INSERT_ID);
            ResultSet resultSet = preparedStatement.executeQuery();
            int id = resultSet.getInt("genre_id");
            return id;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public boolean delete(Genre entity) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(DELETE_GENRE_BY_ID);
            preparedStatement.setInt(1, entity.getGenreId());
            preparedStatement.executeQuery();
            return true;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public Genre findEntityById(int id) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;
        List<Genre> genres;

        try {
            preparedStatement = connection.prepareStatement(SELECT_GENRE_BY_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            genres = createGenreList(resultSet);
            return genres.get(0);
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public List<Genre> findAll() throws DAOTechnicalException {
        List<Genre> genres;
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SELECT_ALL_GENRES);
            ResultSet resultSet = preparedStatement.executeQuery();
            genres = createGenreList(resultSet);
            return genres;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public boolean update(Genre entity) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(UPDATE_GENRE);
            preparedStatement = fillOutStatementByGenre(preparedStatement, entity);
            preparedStatement.executeQuery();
            return true;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        } finally {
            close(preparedStatement);
        }
    }

    private PreparedStatement fillOutStatementByGenre(PreparedStatement preparedStatement, Genre genre)
            throws DAOTechnicalException {
        try {
            preparedStatement.setString(1, genre.getName());
            preparedStatement.setString(2, genre.getDescription());
            preparedStatement.setInt(3, genre.getGenreId());
            return preparedStatement;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        }
    }

    private List<Genre> createGenreList(ResultSet resultSet) throws DAOTechnicalException {
        List<Genre> genres = new LinkedList<>();
        Genre genre;
        try {
            while (resultSet.next()) {
                int genreId = resultSet.getInt("genre_id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                genre = new Genre(genreId, name, description);
                genres.add(genre);
            }
            return genres;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        }
    }
}
