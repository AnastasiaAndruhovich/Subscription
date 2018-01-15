package by.andruhovich.subscription.dao.impl;

import by.andruhovich.subscription.dao.PublicationManagerDAO;
import by.andruhovich.subscription.entity.Publication;
import by.andruhovich.subscription.exception.DAOTechnicalException;
import by.andruhovich.subscription.mapper.PublicationMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PublicationDAO extends PublicationManagerDAO {
    private static final String INSERT_PUBLICATION= "INSERT INTO publications(name, publication_type_id, genre_id, " +
            "description, price) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_LAST_INSERT_ID = "SELECT LAST_INSERT_ID()";
    private static final String DELETE_PUBLICATION_BY_ID = "DELETE FROM publications WHERE publication_id = ?";
    private static final String SELECT_PUBLICATION_BY_ID = "SELECT publication_id, name, description, price " +
            "FROM publications WHERE publication_id = ?";
    private static final String SELECT_ALL_PUBLICATIONS = "SELECT publication_id, name, description, price " +
            "FROM publications";
    private static final String UPDATE_PUBLICATION = "UPDATE publications SET name = ?, publication_type_id = ?, " +
            "genre_id = ?, description = ?, price = ? WHERE publication_id = ?";

    private static final String SELECT_GENRE_ID_BY_PUBLICATION_ID = "SELECT genre_id FROM publications " +
            "WHERE publication_id = ?";
    private static final String SELECT_PUBLICATION_TYPE_ID_BY_PUBLICATION_ID = "SELECT publication_type_id " +
            "FROM publications WHERE publication_id = ?";

    public PublicationDAO(Connection connection) {
        super(connection);
    }

    @Override
    public int create(Publication entity) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;
        PublicationMapper mapper = new PublicationMapper();
        int id = 0;

        try {
            preparedStatement = connection.prepareStatement(INSERT_PUBLICATION);
            preparedStatement = mapper.mapEntityToPreparedStatement(preparedStatement, entity);
            preparedStatement.executeQuery();
            preparedStatement = connection.prepareStatement(SELECT_LAST_INSERT_ID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                id = resultSet.getInt("publication_id");
            }
            return id;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public boolean delete(Publication entity) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(DELETE_PUBLICATION_BY_ID);
            preparedStatement.setInt(1, entity.getPublicationId());
            preparedStatement.executeQuery();
            return true;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public Publication findEntityById(int id) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;
        List<Publication> publications;

        try {
            preparedStatement = connection.prepareStatement(SELECT_PUBLICATION_BY_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            PublicationMapper mapper = new PublicationMapper();
            publications = mapper.mapResultSetToEntity(resultSet);
            return publications.get(0);
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public List<Publication> findAll() throws DAOTechnicalException {
        List<Publication> publications;
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SELECT_ALL_PUBLICATIONS);
            ResultSet resultSet = preparedStatement.executeQuery();
            PublicationMapper mapper = new PublicationMapper();
            publications = mapper.mapResultSetToEntity(resultSet);
            return publications;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public boolean update(Publication entity) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(UPDATE_PUBLICATION);
            PublicationMapper mapper = new PublicationMapper();
            preparedStatement = mapper.mapEntityToPreparedStatement(preparedStatement, entity);
            preparedStatement.executeQuery();
            return true;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public int findGenreIdByPublicationId(int id) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;
        int genreId = 0;

        try {
            preparedStatement = connection.prepareStatement(SELECT_GENRE_ID_BY_PUBLICATION_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                genreId = resultSet.getInt("genre_id");
            }
            return genreId;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public int findPublicationTypeIdByPublicationId(int id) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;
        int genreId = 0;

        try {
            preparedStatement = connection.prepareStatement(SELECT_PUBLICATION_TYPE_ID_BY_PUBLICATION_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                genreId = resultSet.getInt("publication_type_id");
            }
            return genreId;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        } finally {
            close(preparedStatement);
        }
    }
}
