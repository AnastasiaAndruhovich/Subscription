package by.andruhovich.subscription.service;

import by.andruhovich.subscription.dao.DAOFactory;
import by.andruhovich.subscription.dao.impl.AuthorDAO;
import by.andruhovich.subscription.dao.impl.AuthorPublicationDAO;
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
import java.util.LinkedList;
import java.util.List;

public class PublicationService extends BaseService {

    public List<Publication> showPublications(String pageNumber) throws ServiceTechnicalException {
        DAOFactory daoFactory = DAOFactory.getInstance();
        PublicationDAO publicationDAO = null;

        int number = Integer.parseInt(pageNumber);
        int startIndex = (number - 1) * ENTITY_COUNT_FOR_ONE_PAGE;
        int endIndex = startIndex + ENTITY_COUNT_FOR_ONE_PAGE;

        try {
            publicationDAO = daoFactory.createPublicationDAO();
            List<Publication> publications = publicationDAO.findAll(startIndex, endIndex);
            return fillOutPublicationList(publications);
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            daoFactory.closeDAO(publicationDAO);
        }
    }

    public int addPublication(String name, List<String> authorFirstNames, List<String> authorLastNames,
                              String publisherName, String publicationTypeName, String genreName, String description,
                              String price) throws ServiceTechnicalException {

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

        BigDecimal decimalPrice = new BigDecimal(price);
        Publication publication = new Publication(name, description, decimalPrice, authors, genre, publicationType);
        int publicationId = findIdByPublication(publication);
        try {
            publicationDAO = daoFactory.createPublicationDAO();
            if (publicationId != -1) {
                return -1;
            }
            publicationId = publicationDAO.create(publication);
            publication.setPublicationId(publicationId);
            for (Author author : authors) {
                publicationDAO.createRecord(author, publication);
            }

        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            daoFactory.closeDAO(publicationDAO);
        }
        return publicationId;
    }

    public boolean deletePublication(String publicationId) throws ServiceTechnicalException {
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
                                     String genreName, String description, String price) throws ServiceTechnicalException {
        DAOFactory daoFactory = DAOFactory.getInstance();
        PublicationDAO publicationDAO = null;
        AuthorPublicationDAO authorPublicationDAO = null;
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
            publicationDAO = daoFactory.createPublicationDAO();
            byte[] picture = publicationDAO.findPictureByPublicationId(intOldPublicationId);
            String pictureName = publicationDAO.findPictureNameByPublicationId(intOldPublicationId);
            authorPublicationDAO = daoFactory.createAuthorPublicationDAO();
            authorPublicationDAO.deleteRecordByPublicationId(intOldPublicationId);
            Publication publication = new Publication(intOldPublicationId, name, description, decimalPrice, pictureName, picture);
            for (Author author : authors) {
                authorPublicationDAO.createRecord(author, publication);
            }
            publication = new Publication(intOldPublicationId, name, description, decimalPrice, pictureName, picture, authors, genre, publicationType);
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

    public List<Publication> findPublicationByAuthorId(String authorId, String pageNumber) throws ServiceTechnicalException {
        DAOFactory daoFactory = DAOFactory.getInstance();
        AuthorPublicationDAO authorPublicationDAO = null;
        int intAuthorId = Integer.parseInt(authorId);

        int number = Integer.parseInt(pageNumber);
        int startIndex = (number - 1) * ENTITY_COUNT_FOR_ONE_PAGE;
        int endIndex = startIndex + ENTITY_COUNT_FOR_ONE_PAGE;

        try {
            authorPublicationDAO = daoFactory.createAuthorPublicationDAO();
            List<Publication> publications = authorPublicationDAO.findPublicationsByAuthorId(intAuthorId, startIndex, endIndex);
            return fillOutPublicationList(publications);
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            daoFactory.closeDAO(authorPublicationDAO);
        }
    }

    public List<Publication> findPublicationByGenreId(String genreId, String pageNumber) throws ServiceTechnicalException {
        DAOFactory daoFactory = DAOFactory.getInstance();
        PublicationDAO publicationDAO = null;
        int intGenreId = Integer.parseInt(genreId);

        int number = Integer.parseInt(pageNumber);
        int startIndex = (number - 1) * ENTITY_COUNT_FOR_ONE_PAGE;
        int endIndex = startIndex + ENTITY_COUNT_FOR_ONE_PAGE;

        try {
            publicationDAO = daoFactory.createPublicationDAO();
            List<Publication> publications = publicationDAO.findPublicationsByGenreId(intGenreId, startIndex, endIndex);
            return fillOutPublicationList(publications);
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            daoFactory.closeDAO(publicationDAO);
        }
    }

    public List<Publication> findPublicationByPublicationTypeId(String publicationTypeId, String pageNumber)
            throws ServiceTechnicalException {
        DAOFactory daoFactory = DAOFactory.getInstance();
        PublicationDAO publicationDAO = null;
        int intPublicationTypeId = Integer.parseInt(publicationTypeId);

        int number = Integer.parseInt(pageNumber);
        int startIndex = (number - 1) * ENTITY_COUNT_FOR_ONE_PAGE;
        int endIndex = startIndex + ENTITY_COUNT_FOR_ONE_PAGE;

        try {
            publicationDAO = daoFactory.createPublicationDAO();
            List<Publication> publications = publicationDAO.findPublicationsByPublicationTypeId(intPublicationTypeId, startIndex, endIndex);
            return fillOutPublicationList(publications);
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            daoFactory.closeDAO(publicationDAO);
        }
    }

    public int findPublicationPageCount() throws ServiceTechnicalException {
        DAOFactory daoFactory = DAOFactory.getInstance();
        PublicationDAO publicationDAO = null;

        try {
            publicationDAO = daoFactory.createPublicationDAO();
            int count = publicationDAO.findEntityCount();
            return (int) Math.ceil((double) (count) / ENTITY_COUNT_FOR_ONE_PAGE);
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
                List<Author> authors = publicationDAO.findAuthorsByPublicationId(publication.getPublicationId());
                publication.setAuthors(correctAuthorList(authors));
            }
            return publications;
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            daoFactory.closeDAO(publicationDAO);
        }
    }

    public Publication findPublicationById(int publicationId) throws ServiceTechnicalException {
        DAOFactory daoFactory = DAOFactory.getInstance();
        PublicationDAO publicationDAO = null;

        try {
            publicationDAO = daoFactory.createPublicationDAO();
            Publication publication = publicationDAO.findEntityById(publicationId);
            if (publication == null) return null;
            List<Publication> publications = new LinkedList<>();
            publications.add(publication);
            return fillOutPublicationList(publications).get(0);
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            daoFactory.closeDAO(publicationDAO);
        }
    }

    public byte[] findPictureByPublicationId(int publicationId) throws ServiceTechnicalException {
        DAOFactory daoFactory = DAOFactory.getInstance();
        PublicationDAO publicationDAO = null;

        try {
            publicationDAO = daoFactory.createPublicationDAO();
            return publicationDAO.findPictureByPublicationId(publicationId);
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            daoFactory.closeDAO(publicationDAO);
        }
    }

    private List<Author> correctAuthorList(List<Author> authors) {
        for (Author author : authors) {
            if ("-".equals(author.getAuthorFirstName())) {
                author.setAuthorFirstName(null);
            }
            if ("-".equals(author.getAuthorLastName())) {
                author.setAuthorLastName(null);
            }
        }
        return authors;
    }

    public boolean insertImage(String publicationId, byte[] picture, String pictureName) throws ServiceTechnicalException {
        DAOFactory daoFactory = DAOFactory.getInstance();
        PublicationDAO publicationDAO = null;

        int id = Integer.parseInt(publicationId);

        try {
            publicationDAO = daoFactory.createPublicationDAO();
            return publicationDAO.insertImage(id, picture, pictureName);
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            daoFactory.closeDAO(publicationDAO);
        }
    }
}
