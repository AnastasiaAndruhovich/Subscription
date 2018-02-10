package by.andruhovich.subscription.command.common;

import by.andruhovich.subscription.entity.*;
import by.andruhovich.subscription.exception.MissingResourceTechnicalException;
import by.andruhovich.subscription.exception.ServiceTechnicalException;
import by.andruhovich.subscription.manager.PageManager;
import by.andruhovich.subscription.service.*;
import by.andruhovich.subscription.type.ClientType;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ShowEntityList {
    private static final String ERROR_PAGE = "/jsp/error/error.jsp";

    private static final String CLIENT_TYPE = "clientType";

    private static final String PAGE_NUMBER = "pageNumber";
    private static final String PAGE_COUNT = "pageCount";

    private static final Logger LOGGER = LogManager.getLogger(ShowEntityList.class);

    public static String showAuthorList(HttpServletRequest request, HttpServletResponse response) {
        AuthorService authorService = new AuthorService();

        final String PUBLICATION_USER_PAGE = "path.page.user.authorList";
        final String PUBLICATION_ADMIN_PAGE = "path.page.admin.authorList";
        final String PUBLICATION_LIST_ATTRIBUTE = "authors";

        String page;
        PageManager pageManager = PageManager.getInstance();
        HttpSession session = request.getSession();

        String pageNumber = request.getParameter(PAGE_NUMBER);
        pageNumber = (pageNumber == null) ? "1" : pageNumber;

        try {
            List<Author> authors = authorService.showAuthors(pageNumber);
            int pageCount = authorService.findAuthorPageCount();
            session.setAttribute(PUBLICATION_LIST_ATTRIBUTE, authors);
            session.setAttribute(PAGE_NUMBER, pageNumber);
            session.setAttribute(PAGE_COUNT, pageCount);

            ClientType type = (ClientType) session.getAttribute(CLIENT_TYPE);
            if (type.equals(ClientType.ADMIN)) {
                page = pageManager.getProperty(PUBLICATION_ADMIN_PAGE);
            }
            else {
                page = pageManager.getProperty(PUBLICATION_USER_PAGE);
            }
        } catch (ServiceTechnicalException e) {
            LOGGER.log(Level.ERROR, "Database error connection");
            page = ERROR_PAGE;
        } catch (MissingResourceTechnicalException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
            page = ERROR_PAGE;
        }
        return page;
    }

    public static String showPublicationList(HttpServletRequest request, HttpServletResponse response) {
        PublicationService publicationService = new PublicationService();

        final String PUBLICATION_USER_PAGE = "path.page.user.publicationList";
        final String PUBLICATION_ADMIN_PAGE = "path.page.admin.publicationList";
        final String PUBLICATION_LIST_ATTRIBUTE = "publications";

        String page;
        PageManager pageManager = PageManager.getInstance();
        HttpSession session = request.getSession();

        String pageNumber = request.getParameter(PAGE_NUMBER);
        pageNumber = (pageNumber == null) ? "1" : pageNumber;

        try {
            List<Publication> publications = publicationService.showPublications(pageNumber);
            if (!publications.isEmpty()) {
                int pageCount = publicationService.findPublicationPageCount();
                session.setAttribute(PUBLICATION_LIST_ATTRIBUTE, publications);
                session.setAttribute(PAGE_NUMBER, pageNumber);
                session.setAttribute(PAGE_COUNT, pageCount);
            }

            ClientType type = (ClientType) session.getAttribute(CLIENT_TYPE);
            if (type.equals(ClientType.ADMIN)) {
                page = pageManager.getProperty(PUBLICATION_ADMIN_PAGE);
            }
            else {
                page = pageManager.getProperty(PUBLICATION_USER_PAGE);
            }
        } catch (ServiceTechnicalException e) {
            LOGGER.log(Level.ERROR, "Database error connection");
            page = ERROR_PAGE;
        } catch (MissingResourceTechnicalException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
            page = ERROR_PAGE;
        }
        return page;
    }

    public static String showGenreList(HttpServletRequest request, HttpServletResponse response) {
        GenreService genreService = new GenreService();

        final String GENRE_USER_PAGE = "path.page.user.genreList";
        final String GENRE_ADMIN_PAGE = "path.page.admin.genreList";
        final String PUBLICATION_LIST_ATTRIBUTE = "genres";

        String page;
        HttpSession session = request.getSession();
        PageManager pageManager = PageManager.getInstance();

        String pageNumber = request.getParameter(PAGE_NUMBER);
        pageNumber = (pageNumber == null) ? "1" : pageNumber;

        try {
            List<Genre> genres = genreService.showGenres(pageNumber);
            if (!genres.isEmpty()) {
                int pageCount = genreService.findGenrePageCount();
                session.setAttribute(PUBLICATION_LIST_ATTRIBUTE, genres);
                session.setAttribute(PAGE_NUMBER, pageNumber);
                session.setAttribute(PAGE_COUNT, pageCount);
            }

            ClientType type = (ClientType) session.getAttribute(CLIENT_TYPE);
            if (type.equals(ClientType.ADMIN)) {
                page = pageManager.getProperty(GENRE_ADMIN_PAGE);
            }
            else {
                page = pageManager.getProperty(GENRE_USER_PAGE);
            }
        } catch (ServiceTechnicalException e) {
            LOGGER.log(Level.ERROR, "Database error connection");
            page = ERROR_PAGE;
        } catch (MissingResourceTechnicalException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
            page = ERROR_PAGE;
        }
        return page;
    }

    public static String showPublicationTypeList(HttpServletRequest request, HttpServletResponse response) {
        PublicationTypeService publicationTypeService = new PublicationTypeService();

        final String PUBLICATION_TYPE_USER_PAGE = "path.page.user.publicationTypeList";
        final String PUBLICATION_TYPE_ADMIN_PAGE = "path.page.admin.publicationTypeList";
        final String PUBLICATION_LIST_ATTRIBUTE = "publicationTypes";

        String page;
        PageManager pageManager = PageManager.getInstance();
        HttpSession session = request.getSession();

        String pageNumber = request.getParameter(PAGE_NUMBER);
        pageNumber = (pageNumber == null) ? "1" : pageNumber;

        try {
            List<PublicationType> publicationTypes = publicationTypeService.showPublicationTypes(pageNumber);
            int pageCount = publicationTypeService.findPublicationTypePageCount();
            session.setAttribute(PUBLICATION_LIST_ATTRIBUTE, publicationTypes);
            session.setAttribute(PAGE_NUMBER, pageNumber);
            session.setAttribute(PAGE_COUNT, pageCount);

            ClientType type = (ClientType) session.getAttribute(CLIENT_TYPE);
            if (type.equals(ClientType.ADMIN)) {
                page = pageManager.getProperty(PUBLICATION_TYPE_ADMIN_PAGE);
            }
            else {
                page = pageManager.getProperty(PUBLICATION_TYPE_USER_PAGE);
            }
        } catch (ServiceTechnicalException e) {
            LOGGER.log(Level.ERROR, "Database error connection");
            page = ERROR_PAGE;
        } catch (MissingResourceTechnicalException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
            page = ERROR_PAGE;
        }
        return page;
    }

    public static String showUserList(HttpServletRequest request, HttpServletResponse response) {
        UserService userService = new UserService();

        final String USER_ADMIN_PAGE = "path.page.admin.userList";
        final String USER_LIST_ATTRIBUTE = "users";

        String page;
        PageManager pageManager = PageManager.getInstance();
        HttpSession session = request.getSession();

        String pageNumber = request.getParameter(PAGE_NUMBER);
        pageNumber = (pageNumber == null) ? "1" : pageNumber;

        try {
            List<User> users = userService.showUsers(pageNumber);
            int pageCount = userService.findUserPageCount();
            session.setAttribute(USER_LIST_ATTRIBUTE, users);
            session.setAttribute(PAGE_NUMBER, pageNumber);
            session.setAttribute(PAGE_COUNT, pageCount);
            page = pageManager.getProperty(USER_ADMIN_PAGE);
        } catch (ServiceTechnicalException e) {
            LOGGER.log(Level.ERROR, "Database error connection");
            page = ERROR_PAGE;
        } catch (MissingResourceTechnicalException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
            page = ERROR_PAGE;
        }
        return page;
    }

    public static String showSubscriptionList(HttpServletRequest request, HttpServletResponse response) {
        SubscriptionService subscriptionService = new SubscriptionService();

        final String SUBSCRIPTION_USER_PAGE = "path.page.admin.subscriptionList";
        final String SUBSCRIPTION_LIST_ATTRIBUTE = "subscriptions";
        final String CURRENT_DATE_ATTRIBUTE = "currentDate";

        String page;
        PageManager pageManager = PageManager.getInstance();
        HttpSession session = request.getSession();

        String pageNumber = request.getParameter(PAGE_NUMBER);
        pageNumber = (pageNumber == null) ? "1" : pageNumber;

        Date date = Calendar.getInstance().getTime();
        session.setAttribute(CURRENT_DATE_ATTRIBUTE, date);

        try {
            List<Subscription> subscriptions = subscriptionService.showSubscriptions(pageNumber);
            int pageCount = subscriptionService.findSubscriptionPageCount();
            session.setAttribute(SUBSCRIPTION_LIST_ATTRIBUTE, subscriptions);
            session.setAttribute(PAGE_NUMBER, pageNumber);
            session.setAttribute(PAGE_COUNT, pageCount);
            page = pageManager.getProperty(SUBSCRIPTION_USER_PAGE);
        } catch (ServiceTechnicalException e) {
            LOGGER.log(Level.ERROR, "Database error connection");
            page = ERROR_PAGE;
        } catch (MissingResourceTechnicalException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
            page = ERROR_PAGE;
        }
        return page;
    }

    public static String showUser(HttpServletRequest request, HttpServletResponse response) {
        UserService userService = new UserService();

        final String USER_ATTRIBUTE = "user";
        final String CLIENT_ID = "clientId";
        final String EDIT_PROFILE_PAGE = "path.page.user.editProfile";

        String page;
        PageManager pageManager = PageManager.getInstance();
        HttpSession session = request.getSession();

        Integer userId = (Integer) session.getAttribute(CLIENT_ID);

        try {
            User user = userService.findUserById(userId.toString());
            session.setAttribute(USER_ATTRIBUTE, user);
            page = pageManager.getProperty(EDIT_PROFILE_PAGE);
        } catch (ServiceTechnicalException e) {
            LOGGER.log(Level.ERROR, "Database error connection");
            page = ERROR_PAGE;
        } catch (MissingResourceTechnicalException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
            page = ERROR_PAGE;
        }
        return page;
    }

    public static String showSubscriptionByUser(HttpServletRequest request, HttpServletResponse response) {
        SubscriptionService subscriptionService = new SubscriptionService();

        final String USER_ID_ATTRIBUTE = "userId";
        final String CLIENT_ID_ATTRIBUTE = "clientId";
        final String SUBSCRIPTION_LIST_ATTRIBUTE = "subscriptions";
        final String CURRENT_DATE_ATTRIBUTE = "currentDate";

        final String PUBLICATION_USER_PAGE = "path.page.user.subscriptionByUser";

        String page;
        PageManager pageManager = PageManager.getInstance();
        HttpSession session = request.getSession();

        String pageNumber = request.getParameter(PAGE_NUMBER);
        pageNumber = (pageNumber == null) ? "1" : pageNumber;
        String userId = request.getParameter(USER_ID_ATTRIBUTE);
        if (userId == null) {
            userId = session.getAttribute(CLIENT_ID_ATTRIBUTE).toString();
        }
        session.setAttribute(USER_ID_ATTRIBUTE, userId);

        Date date = Calendar.getInstance().getTime();
        session.setAttribute(CURRENT_DATE_ATTRIBUTE, date);

        try {
            List<Subscription> subscriptions = subscriptionService.findSubscriptionByUserId(userId, pageNumber);
            int pageCount = subscriptionService.findSubscriptionByUserPageCount(userId);
            session.setAttribute(SUBSCRIPTION_LIST_ATTRIBUTE, subscriptions);
            session.setAttribute(PAGE_NUMBER, pageNumber);
            session.setAttribute(PAGE_COUNT, pageCount);
            page = pageManager.getProperty(PUBLICATION_USER_PAGE);
        } catch (ServiceTechnicalException e) {
            LOGGER.log(Level.ERROR, "Database error connection");
            page = ERROR_PAGE;
        } catch (MissingResourceTechnicalException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
            page = ERROR_PAGE;
        }
        return page;
    }
}
