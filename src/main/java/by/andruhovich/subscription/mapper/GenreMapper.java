package by.andruhovich.subscription.mapper;

import by.andruhovich.subscription.entity.Genre;
import by.andruhovich.subscription.exception.DAOTechnicalException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Provides methods to prepare Genre entity for setting and getting from database
 */
public class GenreMapper implements EntityMapper<Genre> {
    /**
     * @param resultSet java.sql.ResultSet from database to map on entity
     * @return Genre list from resultSet
     * @throws DAOTechnicalException
     *          If there was an error during mapping resultSet
     */
    @Override
    public List<Genre> mapResultSetToEntity(ResultSet resultSet) throws DAOTechnicalException {
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

    /**
     * @param preparedStatement java.sql.Statement with all necessary parameters
     * @param entity Genre to be set in database
     * @return Filled out statement by entity
     * @throws DAOTechnicalException
     *          If there was an error during mapping resultSet
     */
    @Override
    public PreparedStatement mapEntityToPreparedStatement(PreparedStatement preparedStatement, Genre entity) throws DAOTechnicalException {
        try {
            preparedStatement.setString(1, entity.getName());
            if (entity.getDescription() == null) {
                preparedStatement.setNull(2, java.sql.Types.BLOB);
            }
            else {
                preparedStatement.setString(2, entity.getDescription());
            }
            return preparedStatement;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        }
    }
}
