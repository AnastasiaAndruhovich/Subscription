package by.andruhovich.subscription.dao.impl;

import by.andruhovich.subscription.dao.PublicationTypeManagerDAO;
import by.andruhovich.subscription.entity.Publication;
import by.andruhovich.subscription.entity.PublicationType;
import by.andruhovich.subscription.exception.DAOTechnicalException;
import by.andruhovich.subscription.mapper.PublicationTypeMapper;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PublicationTypeDAO extends PublicationTypeManagerDAO {
    private static final String INSERT_PUBLICATION_TYPE = "INSERT INTO publication_types(name) VALUE (?)";
    private static final String DELETE_PUBLICATION_TYPE_BY_ID = "DELETE FROM publications WHERE publication_id = ?";
    private static final String SELECT_COUNT = "SELECT COUNT(publication_type_id) AS count FROM publication_types";
    private static final String SELECT_PUBLICATION_TYPE_BY_ID = "SELECT publication_type_id, name FROM publication_types " +
            "WHERE publication_type_id = ?";
    private static final String SELECT_ALL_PUBLICATION_TYPES = "SELECT * FROM publications LIMIT ?, ?";
    private static final String UPDATE_PUBLICATION_TYPE = "UPDATE publications SET publication_type_id = ?, name = ?";

    private static final String SELECT_PUBLICATION_TYPE_ID_BY_PUBLICATION_FIELDS = "SELECT publication_type_id " +
            "FROM publication_types WHERE name = ?";

    private static final Logger LOGGER = LogManager.getLogger(PublicationTypeDAO.class);

    public PublicationTypeDAO(Connection connection) {
        super(connection);
    }

    @Override
    public int create(PublicationType entity) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for create publication type");
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
            LOGGER.log(Level.INFO, "Request for create publication type - succeed");
            return id;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public boolean delete(int id) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for delete publication type");
        return delete(id, DELETE_PUBLICATION_TYPE_BY_ID);
    }

    @Override
    public PublicationType findEntityById(int id) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for find entity by id");
        PreparedStatement preparedStatement = null;
        List<PublicationType> publicationTypes;

        try {
            preparedStatement = connection.prepareStatement(SELECT_PUBLICATION_TYPE_BY_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            PublicationTypeMapper publicationTypeMapper = new PublicationTypeMapper();
            publicationTypes = publicationTypeMapper.mapResultSetToEntity(resultSet);
            LOGGER.log(Level.INFO, "Request for find entity by id - succeed");
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
        LOGGER.log(Level.INFO, "Request for find all");
        List<PublicationType> publicationTypes;
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SELECT_ALL_PUBLICATION_TYPES);
            preparedStatement.setInt(1, startIndex);
            preparedStatement.setInt(2, endIndex);
            ResultSet resultSet = preparedStatement.executeQuery();
            PublicationTypeMapper publicationTypeMapper = new PublicationTypeMapper();
            publicationTypes = publicationTypeMapper.mapResultSetToEntity(resultSet);
            LOGGER.log(Level.INFO, "Request for find all - succeed");
            return publicationTypes;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public boolean update(PublicationType entity) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for update publication type");
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(UPDATE_PUBLICATION_TYPE);
            PublicationTypeMapper mapper = new PublicationTypeMapper();
            preparedStatement = mapper.mapEntityToPreparedStatement(preparedStatement, entity);
            preparedStatement.executeQuery();
            LOGGER.log(Level.INFO, "Request for update publication type - succeed");
            return true;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public List<Publication> findPublicationsByPublicationTypeId(int id) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for find publications by publication type id");
        PublicationDAO publicationDAO = new PublicationDAO(connection);
        return publicationDAO.findPublicationsByPublicationTypeId(id);
    }

    @Override
    public int findIdByEntity(PublicationType publicationType) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for find id by entity");
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SELECT_PUBLICATION_TYPE_ID_BY_PUBLICATION_FIELDS);
            preparedStatement.setString(1, publicationType.getName());
            ResultSet resultSet = preparedStatement.executeQuery();
            int id = -1;
            while (resultSet.next()) {
                id = resultSet.getInt("publication_type_id");
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
