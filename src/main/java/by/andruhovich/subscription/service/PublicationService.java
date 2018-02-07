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

public class PublicationService extends BaseService {
    private static final Logger LOGGER = LogManager.getLogger(PublicationService.class);

    public List<Publication> showPublications(String pageNumber) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;

        int number = Integer.parseInt(pageNumber);
        int startIndex = (number - 1) * ENTITY_COUNT_FOR_ONE_PAGE;
        int endIndex = startIndex + ENTITY_COUNT_FOR_ONE_PAGE;

        try {
            connection = connectionFactory.getConnection();
            PublicationManagerDAO publicationManagerDAO = new PublicationDAO(connection);
            List<Publication> publications = publicationManagerDAO.findAll(startIndex, endIndex);
            return FillOutEntityService.fillOutPublicationList(publications);
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

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

    public boolean updatePublication(String oldPublicationId, String name, List<String> authorFirstNames,
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
        int intOldPublicationId = Integer.parseInt(oldPublicationId);

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

    public List<Publication> findPublicationByAuthorId(String authorId, String pageNumber) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;
        int intAuthorId = Integer.parseInt(authorId);

        int number = Integer.parseInt(pageNumber);
        int startIndex = (number - 1) * ENTITY_COUNT_FOR_ONE_PAGE;
        int endIndex = startIndex + ENTITY_COUNT_FOR_ONE_PAGE;

        try {
            connection = connectionFactory.getConnection();
            AuthorPublicationManagerDAO authorPublicationManagerDAO = new AuthorPublicationDAO(connection);
            List<Publication> publications = authorPublicationManagerDAO.findPublicationsByAuthorId(intAuthorId, startIndex, endIndex);
            return FillOutEntityService.fillOutPublicationList(publications);
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    public List<Publication> findPublicationByGenreId(String genreId, String pageNumber) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;
        int intGenreId = Integer.parseInt(genreId);

        int number = Integer.parseInt(pageNumber);
        int startIndex = (number - 1) * ENTITY_COUNT_FOR_ONE_PAGE;
        int endIndex = startIndex + ENTITY_COUNT_FOR_ONE_PAGE;

        try {
            connection = connectionFactory.getConnection();
            PublicationManagerDAO publicationDAO = new PublicationDAO(connection);
            List<Publication> publications = publicationDAO.findPublicationsByGenreId(intGenreId, startIndex, endIndex);
            return FillOutEntityService.fillOutPublicationList(publications);
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    public List<Publication> findPublicationByPublicationTypeId(String publicationTypeId, String pageNumber)
            throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;
        int intPublicationTypeId = Integer.parseInt(publicationTypeId);

        int number = Integer.parseInt(pageNumber);
        int startIndex = (number - 1) * ENTITY_COUNT_FOR_ONE_PAGE;
        int endIndex = startIndex + ENTITY_COUNT_FOR_ONE_PAGE;

        try {
            connection = connectionFactory.getConnection();
            PublicationManagerDAO publicationManagerDAO = new PublicationDAO(connection);
            List<Publication> publications = publicationManagerDAO.findPublicationsByPublicationTypeId(intPublicationTypeId, startIndex, endIndex);
            return FillOutEntityService.fillOutPublicationList(publications);
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

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
