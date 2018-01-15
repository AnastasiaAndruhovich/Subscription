package by.andruhovich.subscription.mapper;

import by.andruhovich.subscription.exception.DAOTechnicalException;
import by.andruhovich.subscription.exception.ResourceTechnicalException;
import by.andruhovich.subscription.entity.Publication;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class PublicationMapper implements EntityMapper<Publication> {
    @Override
    public List<Publication> mapResultSetToEntity(ResultSet resultSet) throws DAOTechnicalException {
        List<Publication> subscriptions = new LinkedList<>();
        Publication publication;
        TypeConverter typeConverter = new TypeConverter();

        try {
            while (resultSet.next()) {
                int publicationId = resultSet.getInt("publication_id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                BigDecimal price = resultSet.getBigDecimal("price");
                publication = new Publication(publicationId, name, description, price);
                subscriptions.add(publication);
            }
            return subscriptions;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        }
    }

    @Override
    public PreparedStatement mapEntityToPreparedStatement(PreparedStatement preparedStatement, Publication entity) throws DAOTechnicalException {
        try {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setInt(2, entity.getPublicationType().getPublicationTypeId());
            preparedStatement.setInt(3, entity.getGenre().getGenreId());
            preparedStatement.setString(4, entity.getDescription());
            preparedStatement.setBigDecimal(5, entity.getPrice());
            return preparedStatement;
        } catch (SQLException | ResourceTechnicalException e ) {
            throw new DAOTechnicalException(e.getMessage());
        }
    }
}
