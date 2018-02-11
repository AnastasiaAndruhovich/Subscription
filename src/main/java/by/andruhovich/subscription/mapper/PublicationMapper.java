package by.andruhovich.subscription.mapper;

import by.andruhovich.subscription.entity.Publication;
import by.andruhovich.subscription.exception.DAOTechnicalException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Provides methods to prepare Publication entity for setting and getting from database
 */
public class PublicationMapper implements EntityMapper<Publication> {
    /**
     * @param resultSet java.sql.ResultSet from database to map on entity
     * @return Publication list from resultSet
     * @throws DAOTechnicalException
     *          If there was an error during mapping resultSet
     */
    @Override
    public List<Publication> mapResultSetToEntity(ResultSet resultSet) throws DAOTechnicalException {
        List<Publication> publications = new LinkedList<>();
        Publication publication;

        try {
            while (resultSet.next()) {
                int publicationId = resultSet.getInt("publication_id");
                String name = resultSet.getString("name");
                String description = resultSet.getString("description");
                BigDecimal price = resultSet.getBigDecimal("price");
                String pictureName = resultSet.getString("picture_name");
                Blob blob = resultSet.getBlob("picture");
                byte[] picture = null;
                if (blob != null) {
                    int blobLength = (int) blob.length();
                    picture = blob.getBytes(1, blobLength);
                    blob.free();
                }
                publication = new Publication(publicationId, name, description, price, pictureName, picture);
                publications.add(publication);
            }
            return publications;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e);
        }
    }

    /**
     * @param preparedStatement java.sql.Statement with all necessary parameters
     * @param entity Publication to be set in database
     * @return Filled out statement by entity
     * @throws DAOTechnicalException
     *          If there was an error during mapping resultSet
     */
    @Override
    public PreparedStatement mapEntityToPreparedStatement(PreparedStatement preparedStatement, Publication entity)
            throws DAOTechnicalException {
        try {
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setInt(2, entity.getPublicationType().getPublicationTypeId());
            preparedStatement.setInt(3, entity.getGenre().getGenreId());
            preparedStatement.setString(4, entity.getDescription());
            preparedStatement.setBigDecimal(5, entity.getPrice());
            if (entity.getPictureName() == null) {
                preparedStatement.setNull(6, Types.VARCHAR);
            } else {
                preparedStatement.setString(6, entity.getPictureName());
            }
            if (entity.getPicture() == null) {
                preparedStatement.setNull(7, Types.BLOB);
            } else {
                InputStream inputStream = new ByteArrayInputStream(entity.getPicture());
                preparedStatement.setBlob(7, inputStream);
            }
            return preparedStatement;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        }
    }
}
