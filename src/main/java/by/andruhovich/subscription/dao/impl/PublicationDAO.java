package by.andruhovich.subscription.dao.impl;

import by.andruhovich.subscription.dao.PublicationManagerDAO;
import by.andruhovich.subscription.entity.Genre;
import by.andruhovich.subscription.entity.Publication;
import by.andruhovich.subscription.entity.PublicationType;
import by.andruhovich.subscription.exception.DAOTechnicalException;
import by.andruhovich.subscription.mapper.GenreMapper;
import by.andruhovich.subscription.mapper.PublicationMapper;
import by.andruhovich.subscription.mapper.PublicationTypeMapper;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.*;
import java.util.List;

/**
 * Concrete DAO extends PublicationManagerDAO
 */
public class PublicationDAO extends PublicationManagerDAO {
    private static final String INSERT_PUBLICATION = "INSERT INTO publications(name, publication_type_id, genre_id, " +
            "description, price, picture_name, picture) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_LAST_INSERT_ID = "SELECT publication_id FROM publications ORDER BY publication_id " +
            "DESC LIMIT 1";
    private static final String SELECT_PUBLICATION_BY_ID = "SELECT publication_id, name, description, price, " +
            "picture_name, picture FROM publications WHERE publication_id = ?";
    private static final String SELECT_ALL_PUBLICATIONS = "SELECT publication_id, name, description, price, " +
            "picture_name, picture FROM publications ORDER BY publication_id DESC LIMIT ?, ?";
    private static final String UPDATE_PUBLICATION = "UPDATE publications SET name = ?, publication_type_id = ?, " +
            "genre_id = ?, description = ?, price = ?, picture_name = ?, picture = ? WHERE publication_id = ?";
    private static final String SELECT_PICTURE_NAME_BY_PUBLICATION_ID = "SELECT picture_name FROM publications WHERE publication_id = ?";

    private static final String SELECT_PUBLICATIONS_BY_GENRE_ID = "SELECT publication_id, name, description, price, " +
            "picture_name, picture FROM publications WHERE genre_id = ? ORDER BY genre_id DESC LIMIT ?, ?";
    private static final String SELECT_GENRE_BY_PUBLICATION_ID = "SELECT g.genre_id, g.name, g.description " +
            "FROM publications JOIN genres g USING (genre_id) WHERE publication_id = ?";
    private static final String SELECT_PUBLICATION_TYPE_BY_PUBLICATION_ID = "SELECT  p.publication_type_id, p.name " +
            "FROM publications JOIN publication_types p USING (publication_type_id) WHERE publication_id = ?";
    private static final String SELECT_PUBLICATIONS_BY_PUBLICATION_TYPE_ID = "SELECT publication_id, name, " +
            "description, price, picture_name, picture FROM publications WHERE publication_type_id = ? " +
            "ORDER BY publication_type_id DESC LIMIT ?, ?";
    private static final String SELECT_PUBLICATION_ID_BY_PUBLICATION_FIELDS = "SELECT publication_id FROM publications " +
            "WHERE name = ? && publication_type_id = ? && genre_id = ? && description = ? && price = ?";
    private static final String SELECT_PICTURE_BY_PUBLICATION_ID = "SELECT picture FROM publications WHERE publication_id = ?";
    private static final String INSERT_IMAGE = "UPDATE publications SET picture = ?, picture_name = ? WHERE publication_id = ?";
    private static final String SELECT_PUBLICATION_BY_SUBSCRIPTION_ID = "SELECT p.publication_id, p.name, p.description, " +
            "p.price, p.picture_name, p.picture FROM subscriptions JOIN publications p USING (publication_id) " +
            "WHERE subscription_id = ?";

    private static final String DELETE_PUBLICATION_BY_ID = "DELETE FROM publications WHERE publication_id = ?";
    private static final String SELECT_COUNT = "SELECT COUNT(publication_id) AS count FROM publications";
    private static final String SELECT_COUNT_BY_AUTHOR_ID = "SELECT COUNT(publication_id) AS count FROM authors_publications WHERE author_id = ?";
    private static final String SELECT_COUNT_BY_GENRE_ID = "SELECT COUNT(publication_id) AS count FROM publications WHERE genre_id = ?";
    private static final String SELECT_COUNT_BY_PUBLICATION_TYPE_ID = "SELECT COUNT(publication_id) AS count FROM publications WHERE publication_type_id = ?";


    private static final Logger LOGGER = LogManager.getLogger(PublicationDAO.class);

    /**
     * @param connection java.sql.Connection to initialize super class
     */
    public PublicationDAO(Connection connection) {
        super(connection);
    }

    /**
     * @param entity Entity to be set in database
     * @return The entity id in database
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    @Override
    public int create(Publication entity) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for create publication");
        PreparedStatement preparedStatement = null;
        PreparedStatement statement = null;
        PublicationMapper mapper = new PublicationMapper();
        int id = -1;

        try {
            preparedStatement = connection.prepareStatement(INSERT_PUBLICATION);
            preparedStatement = mapper.mapEntityToPreparedStatement(preparedStatement, entity);
            preparedStatement.executeUpdate();
            statement = connection.prepareStatement(SELECT_LAST_INSERT_ID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                id = resultSet.getInt("publication_id");
            }
            LOGGER.log(Level.INFO, "Request for create publication - succeed");
            return id;
        } catch (MySQLIntegrityConstraintViolationException e) {
            LOGGER.log(Level.INFO, "Publication is already exist");
            return id;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
            close(statement);
        }
    }

    /**
     * @param id Entity id to be deleted from database
     * @return {@code true} if the operation has been completed successfully
     *         {@code false} otherwise
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    @Override
    public boolean delete(int id) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for delete publication");
        return delete(id, DELETE_PUBLICATION_BY_ID);
    }

    /**
     * @param id Entity id to be found in database
     * @return Entity extends T from database
     * @throws DAOTechnicalException
     *              If there was an error during query execute
     */
    @Override
    public Publication findEntityById(int id) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for find entity by id");
        PreparedStatement preparedStatement = null;
        List<Publication> publications;

        try {
            preparedStatement = connection.prepareStatement(SELECT_PUBLICATION_BY_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            PublicationMapper mapper = new PublicationMapper();
            publications = mapper.mapResultSetToEntity(resultSet);
            LOGGER.log(Level.INFO, "Request for find entity by id - succeed");
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

    /**
     * @param startIndex Entity start index in database
     * @param endIndex Entity end index in database
     * @return Entity List from database
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    @Override
    public List<Publication> findAll(int startIndex, int endIndex) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for find all");
        List<Publication> publications;
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SELECT_ALL_PUBLICATIONS);
            preparedStatement.setInt(1, startIndex);
            preparedStatement.setInt(2, endIndex);
            ResultSet resultSet = preparedStatement.executeQuery();
            PublicationMapper mapper = new PublicationMapper();
            publications = mapper.mapResultSetToEntity(resultSet);
            LOGGER.log(Level.INFO, "Request for find all - succeed");
            return publications;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    /**
     * @param  entity Entity to be updated in database
     * @return {@code true} if the operation has been completed successfully
     *         {@code false} otherwise
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    @Override
    public boolean update(Publication entity) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for update publication");
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(UPDATE_PUBLICATION);
            PublicationMapper mapper = new PublicationMapper();
            preparedStatement = mapper.mapEntityToPreparedStatement(preparedStatement, entity);
            preparedStatement.setInt(8, entity.getPublicationId());
            preparedStatement.executeUpdate();
            LOGGER.log(Level.INFO, "Request for update publication - succeed");
            return true;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    /**
     * @param id Publication id in database
     * @return Genre relevant to publication in database
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    @Override
    public Genre findGenreByPublicationId(int id) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for find genre by publication id");
        PreparedStatement preparedStatement = null;
        List<Genre> genres;

        try {
            preparedStatement = connection.prepareStatement(SELECT_GENRE_BY_PUBLICATION_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            GenreMapper genreMapper = new GenreMapper();
            genres = genreMapper.mapResultSetToEntity(resultSet);
            LOGGER.log(Level.INFO, "Request for find genre by publication id - succeed");
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

    /**
     * @param id Publication id in database
     * @return Publication type relevant to database
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    @Override
    public PublicationType findPublicationTypeByPublicationId(int id) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for find publication type bby publication id");
        PreparedStatement preparedStatement = null;
        List<PublicationType> publicationTypes;

        try {
            preparedStatement = connection.prepareStatement(SELECT_PUBLICATION_TYPE_BY_PUBLICATION_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            PublicationTypeMapper publicationTypeMapper = new PublicationTypeMapper();
            publicationTypes = publicationTypeMapper.mapResultSetToEntity(resultSet);
            LOGGER.log(Level.INFO, "Request for find publication type bby publication id - succeed");
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

    /**
     * @param publication Publication in database
     * @return Relevant id in database
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    @Override
    public int findIdByEntity(Publication publication) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for find id by entity");
        PreparedStatement preparedStatement = null;
        int publicationId = -1;

        try {
            preparedStatement = connection.prepareStatement(SELECT_PUBLICATION_ID_BY_PUBLICATION_FIELDS);
            preparedStatement.setString(1, publication.getName());
            preparedStatement.setInt(2, publication.getPublicationType().getPublicationTypeId());
            preparedStatement.setInt(3, publication.getGenre().getGenreId());
            preparedStatement.setString(4, publication.getDescription());
            preparedStatement.setBigDecimal(5, publication.getPrice());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                publicationId = resultSet.getInt("publication_id");
            }
            LOGGER.log(Level.INFO, "Request for find id by entity - succeed");
            return publicationId;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    /**
     * @param id Genre id in database
     * @param startIndex Publication start index in database
     * @param endIndex Publication end index in database
     * @return Publication list relevant to genre in database
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    @Override
    public List<Publication> findPublicationsByGenreId(int id, int startIndex, int endIndex) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for find publication by genre id");
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SELECT_PUBLICATIONS_BY_GENRE_ID);
            preparedStatement.setInt(1, id);
            preparedStatement.setInt(2, startIndex);
            preparedStatement.setInt(3, endIndex);
            ResultSet resultSet = preparedStatement.executeQuery();
            LOGGER.log(Level.INFO, "Request for find publication by genre id - succeed");
            PublicationMapper publicationMapper = new PublicationMapper();
            return publicationMapper.mapResultSetToEntity(resultSet);
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    /**
     * @param id Publication type id in database
     * @param startIndex Publication start index in database
     * @param endIndex Publication end index in database
     * @return Publication list relevant to publication type in database
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    @Override
    public List<Publication> findPublicationsByPublicationTypeId(int id, int startIndex, int endIndex) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for find publications by publication type id");
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SELECT_PUBLICATIONS_BY_PUBLICATION_TYPE_ID);
            preparedStatement.setInt(1, id);
            preparedStatement.setInt(2, startIndex);
            preparedStatement.setInt(3, endIndex);
            ResultSet resultSet = preparedStatement.executeQuery();
            PublicationMapper publicationMapper = new PublicationMapper();
            List<Publication> publications = publicationMapper.mapResultSetToEntity(resultSet);
            LOGGER.log(Level.INFO, "Request for find publications by publication type id - succeed");
            return publications;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    /**
     * @param id Subscription id in database
     * @return Publication relevant to subscription in database
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    @Override
    public Publication findPublicationBySubscriptionId(int id) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for find publication by subscription id");
        PreparedStatement preparedStatement = null;
        List<Publication> publications;

        try {
            preparedStatement = connection.prepareStatement(SELECT_PUBLICATION_BY_SUBSCRIPTION_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            PublicationMapper publicationMapper = new PublicationMapper();
            publications = publicationMapper.mapResultSetToEntity(resultSet);
            LOGGER.log(Level.INFO, "Request for find publication by subscription id - succeed");
            if (!publications.isEmpty()) {
                return publications.get(0);
            }
            return null;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    /**
     * @return Entity extends T count in database
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    @Override
    public int findEntityCount() throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for get count");
        return findEntityCount(SELECT_COUNT);
    }

    /**
     * @param publicationId Publication id in database
     * @return Picture relevant to publication in database
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    @Override
    public byte[] findPictureByPublicationId(int publicationId) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for find picture by publication id");
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SELECT_PICTURE_BY_PUBLICATION_ID);
            preparedStatement.setInt(1, publicationId);
            ResultSet resultSet = preparedStatement.executeQuery();
            byte[] picture = null;
            while (resultSet.next()) {
                Blob blob = resultSet.getBlob("picture");
                if (blob != null) {
                    int blobLength = (int) blob.length();
                    picture = blob.getBytes(1, blobLength);
                    blob.free();
                }
            }
            LOGGER.log(Level.INFO, "Request for find picture by publication id - succeed");
            return picture;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error", e);
        } finally {
            close(preparedStatement);
        }
    }

    /**
     * @param publicationId Publication id in database
     * @return Picture name relevant to publication in database
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    @Override
    public String findPictureNameByPublicationId(int publicationId) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for find picture name by publication id");
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SELECT_PICTURE_NAME_BY_PUBLICATION_ID);
            preparedStatement.setInt(1, publicationId);
            ResultSet resultSet = preparedStatement.executeQuery();
            String pictureName = null;
            while (resultSet.next()) {
                pictureName = resultSet.getString("picture_name");
            }
            LOGGER.log(Level.INFO, "Request for find picture name by publication id - succeed");
            return pictureName;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error", e);
        } finally {
            close(preparedStatement);
        }
    }

    /**
     * @param publicationId Publication id in database
     * @param picture Picture in bytes
     * @param pictureName String picture name
     * @return {@code true} if the operation has been completed successfully
     *         {@code false} otherwise
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    @Override
    public boolean insertImage(int publicationId, byte[] picture, String pictureName) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for insert image");
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(INSERT_IMAGE);
            Blob blob = new SerialBlob(picture);
            preparedStatement.setBlob(1, blob);
            blob.free();
            preparedStatement.setString(2, pictureName);
            preparedStatement.setInt(3, publicationId);
            preparedStatement.executeUpdate();
            LOGGER.log(Level.INFO, "Request for insert image - succeed");
            return true;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    /**
     * @param authorId Author id in database
     * @return Publication count relevant to author in database
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    @Override
    public int findPublicationCountByAuthorId(int authorId) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for get count by author id");
        return findEntityCountById(SELECT_COUNT_BY_AUTHOR_ID, authorId);
    }

    /**
     * @param genreId Genre id in database
     * @return Publication count relevant to genre in database
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    @Override
    public int findPublicationCountByGenreId(int genreId) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for get count by genre id");
        return findEntityCountById(SELECT_COUNT_BY_GENRE_ID, genreId);
    }

    /**
     * @param publicationTypeId Publication type id in database
     * @return Publication count relevant to publicatino type in databases
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    @Override
    public int findPublicationCountByPublicationTypeId(int publicationTypeId) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for get count by publication type id");
        return findEntityCountById(SELECT_COUNT_BY_PUBLICATION_TYPE_ID, publicationTypeId);
    }
}
