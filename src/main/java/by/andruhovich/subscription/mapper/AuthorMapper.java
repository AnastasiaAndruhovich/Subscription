package by.andruhovich.subscription.mapper;

import by.andruhovich.subscription.exception.DAOTechnicalException;
import by.andruhovich.subscription.mapper.EntityMapper;
import by.andruhovich.subscription.entity.Author;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class AuthorMapper implements EntityMapper<Author> {
    @Override
    public List<Author> mapResultSetToEntity(ResultSet resultSet) throws DAOTechnicalException {
        List<Author> authors = new LinkedList<>();
        Author author;
        try {
            while (resultSet.next()) {
                int authorId = resultSet.getInt("author_id");
                String publisherName = resultSet.getString("publisher_name");
                String auhtorLastname = resultSet.getString("auhtor_lastname");
                String authorFirstname = resultSet.getString("author_firstname");
                author = new Author(authorId, publisherName, auhtorLastname, authorFirstname);
                authors.add(author);
            }
            return authors;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        }
    }

    @Override
    public PreparedStatement mapEntityToPreparedStatement(PreparedStatement preparedStatement, Author entity) throws DAOTechnicalException {
        try {
            preparedStatement.setString(1, entity.getPublisherName());
            preparedStatement.setString(2, entity.getAuthorLastname());
            preparedStatement.setString(3, entity.getAuthorFirstname());
            preparedStatement.setInt(4, entity.getAuthorId());
            return preparedStatement;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        }
    }
}
