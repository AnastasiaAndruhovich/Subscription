package by.andruhovich.subscription.service;

import by.andruhovich.subscription.dao.AuthorPublicationManagerDAO;
import by.andruhovich.subscription.dao.PublicationManagerDAO;
import by.andruhovich.subscription.dao.impl.AuthorPublicationDAO;
import by.andruhovich.subscription.dao.impl.PublicationDAO;
import by.andruhovich.subscription.entity.Author;
import by.andruhovich.subscription.entity.Genre;
import by.andruhovich.subscription.entity.Publication;
import by.andruhovich.subscription.entity.PublicationType;
import by.andruhovich.subscription.exception.ConnectionTechnicalException;
import by.andruhovich.subscription.exception.DAOTechnicalException;
import by.andruhovich.subscription.exception.ServiceTechnicalException;
import by.andruhovich.subscription.pool.ConnectionFactory;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Provides methods to process publication information
 */
public class PublicationService extends BaseService {
    private static final Logger LOGGER = LogManager.getLogger(PublicationService.class);

    /**
     * @param pageNumber Current page from jsp
     * @return Publication list from database
     * @throws ServiceTechnicalException
     *          If there was an error during processing operation
     */
    public List<Publication> showPublications(String pageNumber) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;
        int page = Integer.parseInt(pageNumber);

        try {
            connection = connectionFactory.getConnection();
            PublicationManagerDAO publicationManagerDAO = new PublicationDAO(connection);
            int startIndex = (page - 1) * ENTITY_COUNT_FOR_ONE_PAGE;
            List<Publication> publications = publicationManagerDAO.findAll(startIndex, ENTITY_COUNT_FOR_ONE_PAGE);
            return FillOutEntityService.fillOutPublicationList(publications);
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    /**
     * @param name Publication name
     * @param authorFirstNames Author first name
     * @param authorLastNames Author last name
     * @param publisherName Publisher name
     * @param publicationTypeName Publication type name
     * @param genreName Genre name
     * @param description Publication description
     * @param price Publication price
     * @return Publication id from database
     * @throws ServiceTechnicalException
     *          If there was an error during processing operation
     */
    public int addPublication(String name, List<String> authorFirstNames, List<String> authorLastNames,
                              String publisherName, String publicationTypeName, String genreName, String description,
                              String price) throws ServiceTechnicalException {

        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;

        AuthorService authorService = new AuthorService();
        GenreService genreService = new GenreService();
        PublicationTypeService publicationTypeService = new PublicationTypeService();
        Genre genre;
        PublicationType publicationType;
        int genreId = genreService.findIdByGenreName(genreName);
        int publicationTypeId = publicationTypeService.findIdByPublicationTypeName(publicationTypeName);
        if (genreId == -1) {
            genreId = genreService.addGenre(genreName, null);
        }
        if (publicationTypeId == -1) {
            publicationTypeId = publicationTypeService.addPublicationType(publicationTypeName);
        }
        genre = new Genre(genreId, genreName);
        publicationType = new PublicationType(publicationTypeId, publicationTypeName);

        List<Author> authors = authorService.createAuthorList(authorFirstNames, authorLastNames, publisherName);

        BigDecimal decimalPrice = new BigDecimal(price);
        Publication publication = new Publication(name, description, decimalPrice, authors, genre, publicationType);
        int publicationId = findIdByPublication(publication);

        try {
            connection = connectionFactory.getConnection();
            connection.setAutoCommit(false);
            PublicationDAO publicationDAO = new PublicationDAO(connection);
            if (publicationId != -1) {
                return -1;
            }
            publicationId = publicationDAO.create(publication);
            publication.setPublicationId(publicationId);
            AuthorPublicationManagerDAO authorPublicationDAO = new AuthorPublicationDAO(connection);
            for (Author author : authors) {
                authorPublicationDAO.createRecord(author, publication);
            }
            connection.commit();
            return publicationId;
        } catch (DAOTechnicalException | SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                LOGGER.log(Level.ERROR, "Error roll back transaction");
            }
            throw new ServiceTechnicalException(e);
        } catch (ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException e1) {
                    LOGGER.log(Level.ERROR, "Error set auto commit true");
                }
            }
            connectionFactory.returnConnection(connection);
        }
    }

    /**
     * @param publicationId Publication id in database
     * @return {@code true} if the operation has been completed successfully
     *         {@code false} otherwise
     * @throws ServiceTechnicalException
     *          If there was an error during processing operation
     */
    public boolean deletePublication(String publicationId) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;

        int intPublicationId = Integer.parseInt(publicationId);

        try {
            connection = connectionFactory.getConnection();
            PublicationManagerDAO publicationManagerDAO = new PublicationDAO(connection);
            return publicationManagerDAO.delete(intPublicationId);
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    /**
     * @param publicationId Publication id in database
     * @param name Publication name
     * @param authorFirstNames Author last name
     * @param authorLastNames Author first name
     * @param publisherName Publisher name
     * @param publicationTypeName Publication type name
     * @param genreName Genre name
     * @param description Publication description
     * @param price Publication price
     * @return {@code true} if the operation has been completed successfully
     *         {@code false} otherwise
     * @throws ServiceTechnicalException
     *          If there was an error during processing operation
     */
    public boolean updatePublication(String publicationId, String name, List<String> authorFirstNames,
                                     List<String> authorLastNames, String publisherName, String publicationTypeName,
                                     String genreName, String description, String price) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;

        AuthorService authorService = new AuthorService();
        GenreService genreService = new GenreService();
        PublicationTypeService publicationTypeService = new PublicationTypeService();
        Genre genre;
        PublicationType publicationType;

        int genreId = genreService.findIdByGenreName(genreName);
        int publicationTypeId = publicationTypeService.findIdByPublicationTypeName(publicationTypeName);
        if (genreId == -1) {
            genreId = genreService.addGenre(genreName, null);
        }
        if (publicationTypeId == -1) {
            publicationTypeId = publicationTypeService.addPublicationType(publicationTypeName);
        }

        genre = new Genre(genreId, genreName);
        publicationType = new PublicationType(publicationTypeId, publicationTypeName);
        List<Author> authors = authorService.createAuthorList(authorFirstNames, authorLastNames, publisherName);
        BigDecimal decimalPrice = new BigDecimal(price);
        int intOldPublicationId = Integer.parseInt(publicationId);

        try {
            connection = connectionFactory.getConnection();
            connection.setAutoCommit(false);
            PublicationManagerDAO publicationManagerDAO = new PublicationDAO(connection);
            byte[] picture = publicationManagerDAO.findPictureByPublicationId(intOldPublicationId);
            String pictureName = publicationManagerDAO.findPictureNameByPublicationId(intOldPublicationId);
            AuthorPublicationManagerDAO authorPublicationManagerDAO = new AuthorPublicationDAO(connection);
            authorPublicationManagerDAO.deleteRecordByPublicationId(intOldPublicationId);
            Publication publication = new Publication(intOldPublicationId, name, description, decimalPrice, pictureName, picture);
            for (Author author : authors) {
                authorPublicationManagerDAO.createRecord(author, publication);
            }
            publication = new Publication(intOldPublicationId, name, description, decimalPrice, pictureName, picture, authors, genre, publicationType);
            boolean result = publicationManagerDAO.update(publication);
            connection.commit();
            return result;
        } catch (DAOTechnicalException | SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                LOGGER.log(Level.ERROR, "Error roll back transaction");
            }
            throw new ServiceTechnicalException(e);
        } catch (ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException e1) {
                    LOGGER.log(Level.ERROR, "Error set auto commit true");
                }
            }
            connectionFactory.returnConnection(connection);
        }
    }

    /**
     * @param authorId Author id
     * @param pageNumber Current page number from jsp
     * @return Publication list relevant to author id
     * @throws ServiceTechnicalException
     *          If there was an error during processing operation
     */
    public List<Publication> findPublicationByAuthorId(String authorId, String pageNumber) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;
        int intAuthorId = Integer.parseInt(authorId);
        int page = Integer.parseInt(pageNumber);

        try {
            connection = connectionFactory.getConnection();
            AuthorPublicationManagerDAO authorPublicationManagerDAO = new AuthorPublicationDAO(connection);
            int startIndex = (page - 1) * ENTITY_COUNT_FOR_ONE_PAGE;
            List<Publication> publications = authorPublicationManagerDAO.findPublicationsByAuthorId(intAuthorId, startIndex, ENTITY_COUNT_FOR_ONE_PAGE);
            return FillOutEntityService.fillOutPublicationList(publications);
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    /**
     * @param genreId Genre id
     * @param pageNumber Current page number form jsp
     * @return Publication list relevant to genre id
     * @throws ServiceTechnicalException
     *          If there was an error during processing operation
     */
    public List<Publication> findPublicationByGenreId(String genreId, String pageNumber) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;
        int intGenreId = Integer.parseInt(genreId);
        int page = Integer.parseInt(pageNumber);

        try {
            connection = connectionFactory.getConnection();
            PublicationManagerDAO publicationDAO = new PublicationDAO(connection);
            int startIndex = (page - 1) * ENTITY_COUNT_FOR_ONE_PAGE;
            List<Publication> publications = publicationDAO.findPublicationsByGenreId(intGenreId, startIndex, ENTITY_COUNT_FOR_ONE_PAGE);
            return FillOutEntityService.fillOutPublicationList(publications);
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    /**
     * @param publicationTypeId Publication type id
     * @param pageNumber Current page number from jsp
     * @return Publication list relevant tp publication type id
     * @throws ServiceTechnicalException
     *          If there was an error during processing operation
     */
    public List<Publication> findPublicationByPublicationTypeId(String publicationTypeId, String pageNumber)
            throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;
        int intPublicationTypeId = Integer.parseInt(publicationTypeId);
        int page = Integer.parseInt(pageNumber);

        try {
            connection = connectionFactory.getConnection();
            PublicationManagerDAO publicationManagerDAO = new PublicationDAO(connection);
            int startIndex = (page - 1) * ENTITY_COUNT_FOR_ONE_PAGE;
            List<Publication> publications = publicationManagerDAO.findPublicationsByPublicationTypeId(intPublicationTypeId, startIndex, ENTITY_COUNT_FOR_ONE_PAGE);
            return FillOutEntityService.fillOutPublicationList(publications);
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    /**
     * @return Publication count in database
     * @throws ServiceTechnicalException
     *          If there was an error during processing operation
     */
    public int findPublicationPageCount() throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;

        try {
            connection = connectionFactory.getConnection();
            PublicationManagerDAO publicationManagerDAO = new PublicationDAO(connection);
            int count = publicationManagerDAO.findEntityCount();
            return (int) Math.ceil((double) (count) / ENTITY_COUNT_FOR_ONE_PAGE);
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    /**
     * @param authorId Author id in database
     * @return Publication count relevant to author in database
     * @throws ServiceTechnicalException
     *          If there was an error during processing operation
     */
    public int findPublicationByAuthorIdPageCount(String authorId) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;
        int id = Integer.parseInt(authorId);

        try {
            connection = connectionFactory.getConnection();
            PublicationManagerDAO publicationManagerDAO = new PublicationDAO(connection);
            int count = publicationManagerDAO.findPublicationCountByAuthorId(id);
            return (int) Math.ceil((double) (count) / ENTITY_COUNT_FOR_ONE_PAGE);
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    /**
     * @param genreId Genre id
     * @return Publication count relevant to genre in database
     * @throws ServiceTechnicalException
     *          If there was an error during processing operation
     */
    public int findPublicationByGenreIdPageCount(String genreId) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;
        int id = Integer.parseInt(genreId);

        try {
            connection = connectionFactory.getConnection();
            PublicationManagerDAO publicationManagerDAO = new PublicationDAO(connection);
            int count = publicationManagerDAO.findPublicationCountByGenreId(id);
            return (int) Math.ceil((double) (count) / ENTITY_COUNT_FOR_ONE_PAGE);
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    /**
     * @param publicationTypeId Publication type id
     * @return Publication count relevant to publication type id
     * @throws ServiceTechnicalException
     *          If there was an error during processing operation
     */
    public int findPublicationByPublicationTypeIdPageCount(String publicationTypeId) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;
        int id = Integer.parseInt(publicationTypeId);

        try {
            connection = connectionFactory.getConnection();
            PublicationManagerDAO publicationManagerDAO = new PublicationDAO(connection);
            int count = publicationManagerDAO.findPublicationCountByPublicationTypeId(id);
            return (int) Math.ceil((double) (count) / ENTITY_COUNT_FOR_ONE_PAGE);
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    /**
     * @param publication Publication entity
     * @return Relevant publication id in database
     * @throws ServiceTechnicalException
     *          If there was an error during processing operation
     */
    private int findIdByPublication(Publication publication)
            throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;

        try {
            connection = connectionFactory.getConnection();
            PublicationManagerDAO publicationManagerDAO = new PublicationDAO(connection);
            return publicationManagerDAO.findIdByEntity(publication);
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    /**
     * @param publicationId Publication id
     * @return Publication relevant to id
     * @throws ServiceTechnicalException
     *          If there was an error during processing operation
     */
    public Publication findPublicationById(int publicationId) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;

        try {
            connection = connectionFactory.getConnection();
            PublicationManagerDAO publicationManagerDAO = new PublicationDAO(connection);
            Publication publication = publicationManagerDAO.findEntityById(publicationId);
            if (publication == null) return null;
            List<Publication> publications = new LinkedList<>();
            publications.add(publication);
            return FillOutEntityService.fillOutPublicationList(publications).get(0);
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    /**
     * @param publicationId Publication id
     * @return Relevant picture from database
     * @throws ServiceTechnicalException
     *          If there was an error during processing operation
     */
    public byte[] findPictureByPublicationId(int publicationId) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;

        try {
            connection = connectionFactory.getConnection();
            PublicationManagerDAO publicationManagerDAO = new PublicationDAO(connection);
            return publicationManagerDAO.findPictureByPublicationId(publicationId);
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    /**
     * @param publicationId Publication id
     * @param picture Picture
     * @param pictureName Picture name
     * @return {@code true} if the operation has been completed successfully
     *         {@code false} otherwise
     * @throws ServiceTechnicalException
 *              {@code true} if the operation has been completed successfully
     *         {@code false} otherwise
     */
    public boolean insertImage(String publicationId, byte[] picture, String pictureName) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;
        int id = Integer.parseInt(publicationId);

        try {
            connection = connectionFactory.getConnection();
            PublicationManagerDAO publicationManagerDAO = new PublicationDAO(connection);
            return publicationManagerDAO.insertImage(id, picture, pictureName);
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }
}
