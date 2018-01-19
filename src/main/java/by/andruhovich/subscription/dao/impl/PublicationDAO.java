package by.andruhovich.subscription.dao.impl;

import by.andruhovich.subscription.dao.DAOFactory;
import by.andruhovich.subscription.dao.PublicationManagerDAO;
import by.andruhovich.subscription.entity.Author;
import by.andruhovich.subscription.entity.Genre;
import by.andruhovich.subscription.entity.Publication;
import by.andruhovich.subscription.entity.PublicationType;
import by.andruhovich.subscription.exception.DAOTechnicalException;
import by.andruhovich.subscription.mapper.GenreMapper;
import by.andruhovich.subscription.mapper.PublicationMapper;
import by.andruhovich.subscription.mapper.PublicationTypeMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class PublicationDAO extends PublicationManagerDAO {
    private static final String INSERT_PUBLICATION = "INSERT INTO publications(name, publication_type_id, genre_id, " +
            "description, price, picture_name, picture) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String DELETE_PUBLICATION_BY_ID = "DELETE FROM publications WHERE publication_id = ?";
    private static final String SELECT_PUBLICATION_BY_ID = "SELECT publication_id, name, description, price, " +
            "picture_name, picture FROM publications WHERE publication_id = ?";
    private static final String SELECT_ALL_PUBLICATIONS = "SELECT publication_id, name, description, price, " +
            "picture_name, picture FROM publications LIMIT ?, ?";
    private static final String UPDATE_PUBLICATION = "UPDATE publications SET name = ?, publication_type_id = ?, " +
            "genre_id = ?, description = ?, price = ?, picture_name = ?, picture = ? WHERE publication_id = ?";

    private static final String SELECT_PUBLICATIONS_BY_GENRE_ID = "SELECT publication_id, name, description, price, " +
            "picture_name, picture FROM publications WHERE genre_id = ?";
    private static final String SELECT_GENRE_BY_PUBLICATION_ID = "SELECT g.genre_id, g.name, g.description " +
            "FROM publications JOIN genres g USING (genre_id) WHERE publication_id = ?";
    private static final String SELECT_PUBLICATION_TYPE_BY_PUBLICATION_ID = "SELECT  p.publication_type_id, p.name " +
            "FROM publications JOIN publication_types p USING (publication_type_id) WHERE publication_id = ?";
    private static final String SELECT_PUBLICATIONS_BY_PUBLICATION_TYPE_ID = "SELECT publication_id, name, " +
            "description, price, picture_name, picture FROM publications WHERE publication_type_id = ?";

    public PublicationDAO(Connection connection) {
        super(connection);
    }

    @Override
    public int create(Publication entity) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;
        PublicationMapper mapper = new PublicationMapper();
        int id = -1;

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
            throw new DAOTechnicalException("Execute statement error. ", e);
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
            throw new DAOTechnicalException("Execute statement error. ", e);
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
            if (publications.isEmpty()) {
                return null;
            }
            return publications.get(0);
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public List<Publication> findAll(int startIndex, int endIndex) throws DAOTechnicalException {
        List<Publication> publications;
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SELECT_ALL_PUBLICATIONS);
            preparedStatement.setInt(1, startIndex);
            preparedStatement.setInt(2, endIndex);
            ResultSet resultSet = preparedStatement.executeQuery();
            PublicationMapper mapper = new PublicationMapper();
            publications = mapper.mapResultSetToEntity(resultSet);
            return publications;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
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
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public Genre findGenreByPublicationId(int id) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;
        List<Genre> genres;

        try {
            preparedStatement = connection.prepareStatement(SELECT_GENRE_BY_PUBLICATION_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            GenreMapper genreMapper = new GenreMapper();
            genres = genreMapper.mapResultSetToEntity(resultSet);
            if (!genres.isEmpty()) {
                return genres.get(0);
            }
            return null;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public PublicationType findPublicationTypeByPublicationId(int id) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;
        List<PublicationType> publicationTypes;

        try {
            preparedStatement = connection.prepareStatement(SELECT_PUBLICATION_TYPE_BY_PUBLICATION_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            PublicationTypeMapper publicationTypeMapper = new PublicationTypeMapper();
            publicationTypes = publicationTypeMapper.mapResultSetToEntity(resultSet);
            if (!publicationTypes.isEmpty()) {
                return publicationTypes.get(0);
            }
            return null;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public List<Author> findAuthorsByPublicationId(int id) throws DAOTechnicalException {
        AuthorPublicationDAO authorPublicationDAO = new AuthorPublicationDAO(connection);
        return authorPublicationDAO.findAuthorByPublicationId(id);
    }

    @Override
    public List<Publication> findPublicationsByGenreId(int id) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SELECT_PUBLICATIONS_BY_GENRE_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            PublicationMapper publicationMapper = new PublicationMapper();
            return publicationMapper.mapResultSetToEntity(resultSet);
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public List<Publication> findPublicationsByPublicationTypeId(int id) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SELECT_PUBLICATIONS_BY_PUBLICATION_TYPE_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            PublicationMapper publicationMapper = new PublicationMapper();
            List<Publication> publications = publicationMapper.mapResultSetToEntity(resultSet);
            return publications;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }
}
