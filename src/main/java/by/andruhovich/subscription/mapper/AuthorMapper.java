package by.andruhovich.subscription.mapper;

import by.andruhovich.subscription.entity.Author;
import by.andruhovich.subscription.exception.DAOTechnicalException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Provides methods to prepare Author entity for setting and getting from database
 */
public class AuthorMapper implements EntityMapper<Author> {
    /**
     * @param resultSet java.sql.ResultSet from database to map on entity
     * @return Author list from resultSet
     * @throws DAOTechnicalException
     *          If there was an error during mapping resultSet
     */
    @Override
    public List<Author> mapResultSetToEntity(ResultSet resultSet) throws DAOTechnicalException {
        List<Author> authors = new LinkedList<>();
        Author author;
        try {
            while (resultSet.next()) {
                int authorId = resultSet.getInt("author_id");
                String publisherName = resultSet.getString("publisher_name");
                String authorLastName = resultSet.getString("author_lastname");
                String authorFirstName = resultSet.getString("author_firstname");
                author = new Author(authorId, publisherName, authorLastName, authorFirstName);
                authors.add(author);
            }
            return authors;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        }
    }

    /**
     * @param preparedStatement java.sql.Statement with all necessary parameters
     * @param entity Author to be set in database
     * @return Filled out statement by entity
     * @throws DAOTechnicalException
     *          If there was an error during mapping resultSet
     */
    @Override
    public PreparedStatement mapEntityToPreparedStatement(PreparedStatement preparedStatement, Author entity) throws DAOTechnicalException {
        try {
            preparedStatement.setString(1, entity.getPublisherName());
            preparedStatement.setString(2, entity.getAuthorLastName());
            preparedStatement.setString(3, entity.getAuthorFirstName());
            return preparedStatement;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        }
    }
}
