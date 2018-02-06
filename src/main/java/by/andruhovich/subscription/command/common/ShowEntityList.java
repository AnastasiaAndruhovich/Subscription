package by.andruhovich.subscription.command.common;

import by.andruhovich.subscription.command.user.ShowUserCommand;
import by.andruhovich.subscription.entity.*;
import by.andruhovich.subscription.exception.MissingResourceTechnicalException;
import by.andruhovich.subscription.exception.ServiceTechnicalException;
import by.andruhovich.subscription.manager.LocaleManager;
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
import java.util.Locale;

public class ShowEntityList {
    private static final String ERROR_PAGE = "/jsp/error/error.jsp";

    private static final String CLIENT_TYPE = "clientType";
    private static final String LOCALE = "locale";

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

        String pageNumber = request.getParameter(PAGE_NUMBER);
        pageNumber = (pageNumber == null) ? "1" : pageNumber;

        try {
            List<Author> authors = authorService.showAuthors(pageNumber);
            if (!authors.isEmpty()) {
                int pageCount = authorService.findAuthorPageCount();
                request.setAttribute(PUBLICATION_LIST_ATTRIBUTE, authors);
                request.setAttribute(PAGE_NUMBER, pageNumber);
                request.setAttribute(PAGE_COUNT, pageCount);
            }

            HttpSession session = request.getSession();
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

        String pageNumber = request.getParameter(PAGE_NUMBER);
        pageNumber = (pageNumber == null) ? "1" : pageNumber;

        try {
            List<Publication> publications = publicationService.showPublications(pageNumber);
            if (!publications.isEmpty()) {
                int pageCount = publicationService.findPublicationPageCount();
                request.setAttribute(PUBLICATION_LIST_ATTRIBUTE, publications);
                request.setAttribute(PAGE_NUMBER, pageNumber);
                request.setAttribute(PAGE_COUNT, pageCount);
            }

            HttpSession session = request.getSession();
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
        PageManager pageManager = PageManager.getInstance();

        String pageNumber = request.getParameter(PAGE_NUMBER);
        pageNumber = (pageNumber == null) ? "1" : pageNumber;

        try {
            List<Genre> genres = genreService.showGenres(pageNumber);
            if (!genres.isEmpty()) {
                int pageCount = genreService.findGenrePageCount();
                request.setAttribute(PUBLICATION_LIST_ATTRIBUTE, genres);
                request.setAttribute(PAGE_NUMBER, pageNumber);
                request.setAttribute(PAGE_COUNT, pageCount);
            }

            HttpSession session = request.getSession();
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
        Locale locale = (Locale)request.getSession().getAttribute(LOCALE);
        LocaleManager localeManager = new LocaleManager(locale);

        String pageNumber = request.getParameter(PAGE_NUMBER);
        pageNumber = (pageNumber == null) ? "1" : pageNumber;

        try {
            List<PublicationType> publicationTypes = publicationTypeService.showPublicationTypes(pageNumber);
            if (!publicationTypes.isEmpty()) {
                int pageCount = publicationTypeService.findPublicationTypePageCount();
                request.setAttribute(PUBLICATION_LIST_ATTRIBUTE, publicationTypes);
                request.setAttribute(PAGE_NUMBER, pageNumber);
                request.setAttribute(PAGE_COUNT, pageCount);
            }

            HttpSession session = request.getSession();
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

        String pageNumber = request.getParameter(PAGE_NUMBER);
        pageNumber = (pageNumber == null) ? "1" : pageNumber;

        try {
            List<User> users = userService.showUsers(pageNumber);
            if (!users.isEmpty()) {
                int pageCount = userService.findUserPageCount();
                request.setAttribute(USER_LIST_ATTRIBUTE, users);
                request.setAttribute(PAGE_NUMBER, pageNumber);
                request.setAttribute(PAGE_COUNT, pageCount);
            }
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

    public static String findSubscriptionByUser(HttpServletRequest request, HttpServletResponse response) {
        SubscriptionService subscriptionService = new SubscriptionService();

        final String CLIENT_ID_ATTRIBUTE = "clientId";
        final String SUBSCRIPTION_LIST_ATTRIBUTE = "subscriptions";
        final String CURRENT_DATE_ATTRIBUTE = "currentDate";

        final String PUBLICATION_USER_PAGE = "path.page.user.subscriptionList";

        String page;
        PageManager pageManager = PageManager.getInstance();

        String pageNumber = request.getParameter(PAGE_NUMBER);
        pageNumber = (pageNumber == null) ? "1" : pageNumber;
        Integer clientId = (Integer) request.getSession().getAttribute(CLIENT_ID_ATTRIBUTE);
        Date date = Calendar.getInstance().getTime();
        request.setAttribute(CURRENT_DATE_ATTRIBUTE, date);

        try {
            List<Subscription> subscriptions = subscriptionService.findSubscriptionByUserId(clientId.toString(), pageNumber);
            if (!subscriptions.isEmpty()) {
                int pageCount = subscriptionService.findSubscriptionPageCount();
                request.setAttribute(SUBSCRIPTION_LIST_ATTRIBUTE, subscriptions);
                request.setAttribute(PAGE_NUMBER, pageNumber);
                request.setAttribute(PAGE_COUNT, pageCount);
            }
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
