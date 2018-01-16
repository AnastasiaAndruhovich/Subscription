package by.andruhovich.subscription.dao.impl;

import by.andruhovich.subscription.dao.MediatorManagerDAO;
import by.andruhovich.subscription.entity.PublicationType;
import by.andruhovich.subscription.exception.DAOTechnicalException;
import by.andruhovich.subscription.mapper.PublicationTypeMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PublicationTypeDAO extends MediatorManagerDAO<PublicationType> {
    private static final String INSERT_PUBLICATION_TYPE = "INSERT INTO publication_types(name) VALUE (?)";
    private static final String DELETE_PUBLICATION_TYPE_BY_ID = "DELETE FROM publications WHERE publication_id = ?";
    private static final String SELECT_ALL_PUBLICATION_TYPES = "SELECT * FROM publications";
    private static final String UPDATE_PUBLICATION_TYPE = "UPDATE publications SET publication_type_id = ?, name = ?";

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
            throw new DAOTechnicalException(e.getMessage());
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public boolean delete(PublicationType entity) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(DELETE_PUBLICATION_TYPE_BY_ID);
            preparedStatement.setInt(1, entity.getPublicationTypeId());
            preparedStatement.executeQuery();
            return true;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public PublicationType findEntityById(int id) throws DAOTechnicalException {
        return null;
    }

    @Override
    public List<PublicationType> findAll() throws DAOTechnicalException {
        return null;
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
            throw new DAOTechnicalException(e.getMessage());
        } finally {
            close(preparedStatement);
        }
    }

}
