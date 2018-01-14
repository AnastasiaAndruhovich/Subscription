package by.andruhovich.subscription.mapper;

import by.andruhovich.subscription.exception.DAOTechnicalException;
import by.andruhovich.subscription.mapper.EntityMapper;
import by.andruhovich.subscription.entity.Genre;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class GenreMapper implements EntityMapper<Genre> {
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

    @Override
    public PreparedStatement mapEntityToPreparedStatement(PreparedStatement preparedStatement, Genre entity) throws DAOTechnicalException {
        try {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getDescription());
            preparedStatement.setInt(3, entity.getGenreId());
            return preparedStatement;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        }
    }
}
