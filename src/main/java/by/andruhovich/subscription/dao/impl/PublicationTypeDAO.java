package by.andruhovich.subscription.dao.impl;

import by.andruhovich.subscription.dao.PublicationTypeManagerDAO;
import by.andruhovich.subscription.entity.Publication;
import by.andruhovich.subscription.entity.PublicationType;
import by.andruhovich.subscription.exception.DAOTechnicalException;
import by.andruhovich.subscription.mapper.PublicationTypeMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PublicationTypeDAO extends PublicationTypeManagerDAO {
    private static final String INSERT_PUBLICATION_TYPE = "INSERT INTO publication_types(name) VALUE (?)";
    private static final String DELETE_PUBLICATION_TYPE_BY_ID = "DELETE FROM publications WHERE publication_id = ?";
    private static final String SELECT_PUBLICATION_TYPE_BY_ID = "SELECT publication_type_id, name FROM publication_types " +
            "WHERE publication_type_id = ?";
    private static final String SELECT_ALL_PUBLICATION_TYPES = "SELECT * FROM publications LIMIT ?, ?";
    private static final String UPDATE_PUBLICATION_TYPE = "UPDATE publications SET publication_type_id = ?, name = ?";

    private static final String SELECT_PUBLICATION_TYPE_ID_BY_PUBLICATION_FIELDS = "SELECT publication_type_id " +
            "FROM publication_types WHERE name = ?";

    public PublicationTypeDAO(Connection connection) {
        super(connection);
    }

    @Override
    public int create(PublicationType entity) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;
        PublicationTypeMapper mapper = new PublicationTypeMapper();
        int id = -1;

        try {
            preparedStatement = connection.prepareStatement(INSERT_PUBLICATION_TYPE);
            preparedStatement = mapper.mapEntityToPreparedStatement(preparedStatement, entity);
            preparedStatement.executeQuery();
            preparedStatement = connection.prepareStatement(SELECT_LAST_INSERT_ID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                id = resultSet.getInt("publication_type_id");
            }
            return id;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public boolean delete(int id) throws DAOTechnicalException {
        return delete(id, DELETE_PUBLICATION_TYPE_BY_ID);
    }

    @Override
    public PublicationType findEntityById(int id) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;
        List<PublicationType> publicationTypes;

        try {
            preparedStatement = connection.prepareStatement(SELECT_PUBLICATION_TYPE_BY_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            PublicationTypeMapper publicationTypeMapper = new PublicationTypeMapper();
            publicationTypes = publicationTypeMapper.mapResultSetToEntity(resultSet);
            if (publicationTypes.isEmpty()) {
                return null;
            }
            return publicationTypes.get(0);
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public List<PublicationType> findAll(int startIndex, int endIndex) throws DAOTechnicalException {
        List<PublicationType> publicationTypes;
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SELECT_ALL_PUBLICATION_TYPES);
            preparedStatement.setInt(1, startIndex);
            preparedStatement.setInt(2, endIndex);
            ResultSet resultSet = preparedStatement.executeQuery();
            PublicationTypeMapper publicationTypeMapper = new PublicationTypeMapper();
            publicationTypes = publicationTypeMapper.mapResultSetToEntity(resultSet);
            return publicationTypes;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public boolean update(PublicationType entity) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(UPDATE_PUBLICATION_TYPE);
            PublicationTypeMapper mapper = new PublicationTypeMapper();
            preparedStatement = mapper.mapEntityToPreparedStatement(preparedStatement, entity);
            preparedStatement.executeQuery();
            return true;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public List<Publication> findPublicationsByPublicationTypeId(int id) throws DAOTechnicalException {
        PublicationDAO publicationDAO = new PublicationDAO(connection);
        return publicationDAO.findPublicationsByPublicationTypeId(id);
    }

    @Override
    public int findIdByEntity(PublicationType publicationType) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SELECT_PUBLICATION_TYPE_ID_BY_PUBLICATION_FIELDS);
            preparedStatement.setString(1, publicationType.getName());
            ResultSet resultSet = preparedStatement.executeQuery();
            int id = -1;
            while (resultSet.next()) {
                id = resultSet.getInt("publication_type_id");
            }
            return id;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }
}
