package by.andruhovich.subscription.service;

import by.andruhovich.subscription.dao.DAOFactory;
import by.andruhovich.subscription.dao.impl.AuthorDAO;
import by.andruhovich.subscription.dao.impl.PublicationDAO;
import by.andruhovich.subscription.entity.Author;
import by.andruhovich.subscription.entity.Genre;
import by.andruhovich.subscription.entity.Publication;
import by.andruhovich.subscription.entity.PublicationType;
import by.andruhovich.subscription.exception.ConnectionTechnicalException;
import by.andruhovich.subscription.exception.DAOTechnicalException;
import by.andruhovich.subscription.exception.ServiceTechnicalException;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;

public class PublicationService {
    private static final int ENTITY_QUANTITY_FOR_ONE_PAGE = 10;

    public List<Publication> showPublications(String pageNumber) throws ServiceTechnicalException {
        DAOFactory daoFactory = DAOFactory.getInstance();
        PublicationDAO publicationDAO = null;

        int number = Integer.parseInt(pageNumber);
        int startIndex = (number - 1) * ENTITY_QUANTITY_FOR_ONE_PAGE;
        int endIndex = startIndex + ENTITY_QUANTITY_FOR_ONE_PAGE ;

        try {
            publicationDAO = daoFactory.createPublicationDAO();
            List<Publication> publications =  publicationDAO.findAll(startIndex, endIndex);
            return fillOutPublicationList(publications);
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            daoFactory.closeDAO(publicationDAO);
        }
    }

    public int addPublication(String name, List<String> authorFirstNames, List<String> authorLastNames,
                                  String publisherName, String publicationTypeName, String genreName, String description,
                                  String price, String pictureName, InputStream picture) throws ServiceTechnicalException {

        DAOFactory daoFactory = DAOFactory.getInstance();
        PublicationDAO publicationDAO = null;
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

        byte[] pictureByte = PictureConverter.convertFileToByteArray(picture);
        BigDecimal decimalPrice = new BigDecimal(price);
        Publication publication = new Publication(name, description, decimalPrice, pictureName, pictureByte,
                authors, genre, publicationType);
        int publicationId = findIdByPublication(publication);
        try {
            publicationDAO = daoFactory.createPublicationDAO();
            if (publicationId != -1) {
                return -1;
            }
            publicationId = publicationDAO.create(publication);
            publication.setPublicationId(publicationId);
            for(Author author : authors) {
                publicationDAO.createRecord(author, publication);
            }

        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            daoFactory.closeDAO(publicationDAO);
        }
        return publicationId;
    }

    public boolean deletePublication(String publicationId) throws ServiceTechnicalException{
        DAOFactory daoFactory = DAOFactory.getInstance();
        PublicationDAO publicationDAO = null;

        int intPublicationId = Integer.parseInt(publicationId);

        try {
            publicationDAO = daoFactory.createPublicationDAO();
            return publicationDAO.delete(intPublicationId);
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            daoFactory.closeDAO(publicationDAO);
        }
    }

    public boolean updatePublication(String oldPublicationId, String name, List<String> authorFirstNames,
                                     List<String> authorLastNames, String publisherName, String publicationTypeName,
                                     String genreName, String description, String price, String pictureName,
                                     InputStream picture) throws ServiceTechnicalException {
        DAOFactory daoFactory = DAOFactory.getInstance();
        PublicationDAO publicationDAO = null;
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

        byte[] pictureByte = PictureConverter.convertFileToByteArray(picture);
        BigDecimal decimalPrice = new BigDecimal(price);
        int intOldPublicationId = Integer.parseInt(oldPublicationId);
        Publication publication = new Publication(intOldPublicationId, name, description, decimalPrice, pictureName,
                pictureByte, authors, genre, publicationType);
        try {
            publicationDAO = daoFactory.createPublicationDAO();
            return publicationDAO.update(publication);
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            daoFactory.closeDAO(publicationDAO);
        }
    }

    public List<Publication> findPublicationByName(String name) throws ServiceTechnicalException {
        DAOFactory daoFactory = DAOFactory.getInstance();
        PublicationDAO publicationDAO = null;

        try {
            publicationDAO = daoFactory.createPublicationDAO();
            List<Publication> publications = publicationDAO.findPublicationByName(name);
            return fillOutPublicationList(publications);
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            daoFactory.closeDAO(publicationDAO);
        }
    }

    public List<Publication> findPublicationByAuthor(String authorId) throws ServiceTechnicalException {
        DAOFactory daoFactory = DAOFactory.getInstance();
        AuthorDAO authorDAO = null;
        int intAuthorId = Integer.parseInt(authorId);

        try {
            authorDAO = daoFactory.createAuthorDAO();
            List<Publication> publications = authorDAO.findPublicationsByAuthorId(intAuthorId);
            return fillOutPublicationList(publications);
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            daoFactory.closeDAO(authorDAO);
        }
    }

    public List<Publication> findPublicationByGenreId(String genreId) throws ServiceTechnicalException {
        DAOFactory daoFactory = DAOFactory.getInstance();
        PublicationDAO publicationDAO = null;
        int intGenreId = Integer.parseInt(genreId);

        try {
            publicationDAO = daoFactory.createPublicationDAO();
            List<Publication> publications = publicationDAO.findPublicationsByGenreId(intGenreId);
            return fillOutPublicationList(publications);
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            daoFactory.closeDAO(publicationDAO);
        }
    }

    public List<Publication> findPublicationByPublicationTypeId(String publicationTypeId)
            throws ServiceTechnicalException {
        DAOFactory daoFactory = DAOFactory.getInstance();
        PublicationDAO publicationDAO = null;
        int intPublicationTypeId = Integer.parseInt(publicationTypeId);

        try {
            publicationDAO = daoFactory.createPublicationDAO();
            List<Publication> publications = publicationDAO.findPublicationsByPublicationTypeId(intPublicationTypeId);
            return fillOutPublicationList(publications);
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            daoFactory.closeDAO(publicationDAO);
        }
    }

    public int findPublicationCount() throws ServiceTechnicalException {
        DAOFactory daoFactory = DAOFactory.getInstance();
        PublicationDAO publicationDAO = null;

        try {
            publicationDAO = daoFactory.createPublicationDAO();
            return publicationDAO.findEntityCount();
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            daoFactory.closeDAO(publicationDAO);
        }
    }

    private int findIdByPublication(Publication publication)
            throws ServiceTechnicalException {
        DAOFactory daoFactory = DAOFactory.getInstance();
        PublicationDAO publicationDAO = null;

        try {
            publicationDAO = daoFactory.createPublicationDAO();
            return publicationDAO.findIdByEntity(publication);
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            daoFactory.closeDAO(publicationDAO);
        }
    }

    private List<Publication> fillOutPublicationList(List<Publication> publications) throws ServiceTechnicalException {
        DAOFactory daoFactory = DAOFactory.getInstance();
        PublicationDAO publicationDAO = null;
        try {
            publicationDAO = daoFactory.createPublicationDAO();
            for (Publication publication : publications) {
                publication.setGenre(publicationDAO.findGenreByPublicationId(publication.getPublicationId()));
                publication.setPublicationType(publicationDAO.findPublicationTypeByPublicationId(publication.getPublicationId()));
                publication.setAuthors(publicationDAO.findAuthorsByPublicationId(publication.getPublicationId()));
            }
            return publications;
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            daoFactory.closeDAO(publicationDAO);
        }
    }
}
