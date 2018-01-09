package by.andruhovich.subscription.dao;

import by.andruhovich.subscription.entity.Publication;
import by.andruhovich.subscription.exception.DAOTechnicalException;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class PublicationDAO extends PublicationManagerDAO {
    private static final String INSERT_PUBLICATION= "INSERT INTO publications(name, publication_type_id, genre_id, " +
            "description, price) VALUES (?, ?, ?, ?, ?)";
    private static final String SELECT_LAST_INSERT_ID = "SELECT LAST_INSERT_ID()";
    private static final String DELETE_PUBLICATION_BY_ID = "DELETE FROM publications WHERE publication_id = ?";
    private static final String SELECT_PUBLICATION_BY_ID = "SELECT * FROM publications WHERE publication_id = ?";
    private static final String SELECT_ALL_PUBLICATIONS = "SELECT * FROM publications";
    private static final String UPDATE_PUBLICATION = "UPDATE publications SET name = ?, publication_type_id = ?, " +
            "genre_id = ?, description = ?, price = ? WHERE publication_id = ?";

    public PublicationDAO(Connection connection) {
        super(connection);
    }

    @Override
    public int create(Publication entity) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(INSERT_PUBLICATION);
            preparedStatement = fillOutStatementByPublication(preparedStatement, entity);
            preparedStatement.executeQuery();
            preparedStatement = connection.prepareStatement(SELECT_LAST_INSERT_ID);
            ResultSet resultSet = preparedStatement.executeQuery();
            int id = resultSet.getInt("genre_id");
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
            publications = createPublicationList(resultSet);
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
            publications = createPublicationList(resultSet);
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
            preparedStatement = fillOutStatementByPublication(preparedStatement, entity);
            preparedStatement.executeQuery();
            return true;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        } finally {
            close(preparedStatement);
        }
    }

    private PreparedStatement fillOutStatementByPublication(PreparedStatement preparedStatement, Publication publication)
            throws DAOTechnicalException {
        try {
            preparedStatement.setString(1, publication.getName());
            preparedStatement.setInt(2, publication.getPublicationTypeId());
            preparedStatement.setInt(3, publication.getGenreId());
            preparedStatement.setString(4, publication.getDescription());
            preparedStatement.setBigDecimal(5, publication.getPrice());
            return preparedStatement;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        }
    }

    private List<Publication> createPublicationList(ResultSet resultSet) throws DAOTechnicalException {
        List<Publication> publications = new LinkedList<>();
        Publication publication;

        try {
            while (resultSet.next()) {
                int publicationId = resultSet.getInt("publication_id");
                String name = resultSet.getString("name");
                int publicationTypeId = resultSet.getInt("publication_type_id");
                int genreId = resultSet.getInt("genre_id");
                String description = resultSet.getString("description");
                BigDecimal price = new BigDecimal("price");
                publication = new Publication(publicationId, name, publicationTypeId, genreId, description, price);
                publications.add(publication);
            }
            return publications;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        }
    }
}
