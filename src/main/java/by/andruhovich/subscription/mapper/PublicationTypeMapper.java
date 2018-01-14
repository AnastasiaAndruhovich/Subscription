package by.andruhovich.subscription.mapper;

import by.andruhovich.subscription.entity.PublicationType;
import by.andruhovich.subscription.exception.DAOTechnicalException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class PublicationTypeMapper implements EntityMapper<PublicationType> {
    @Override
    public List<PublicationType> mapResultSetToEntity(ResultSet resultSet) throws DAOTechnicalException {
        List<PublicationType> publicationTypes = new LinkedList<>();
        PublicationType publicationType;

        try {
            while (resultSet.next()) {
                int publicationTypeId = resultSet.getInt("publication_type_id");
                String name = resultSet.getString("name");
                publicationType = new PublicationType(publicationTypeId, name);
                publicationTypes.add(publicationType);
            }
            return publicationTypes;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        }
    }

    @Override
    public PreparedStatement mapEntityToPreparedStatement(PreparedStatement preparedStatement, PublicationType entity) throws DAOTechnicalException {
        try {
            preparedStatement.setString(1, entity.getName());
            return preparedStatement;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        }
    }
}
