package by.andruhovich.subscription.command.common;

import by.andruhovich.subscription.entity.Author;
import by.andruhovich.subscription.entity.Publication;
import by.andruhovich.subscription.exception.MissingResourceTechnicalException;
import by.andruhovich.subscription.exception.ServiceTechnicalException;
import by.andruhovich.subscription.manager.LocaleManager;
import by.andruhovich.subscription.manager.PageManager;
import by.andruhovich.subscription.service.AuthorService;
import by.andruhovich.subscription.service.PublicationService;
import by.andruhovich.subscription.type.ClientType;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Locale;

public class ShowEntityList {
    private static final String ERROR_PAGE = "/jsp/error/error.jsp";

    private static final String INFORMATION_IS_ABSENT_ATTRIBUTE = "informationIsAbsent";
    private static final String INFORMATION_IS_ABSENT_MESSAGE = "message.informationIsAbsent";

    private static final String CLIENT_TYPE = "clientType";
    private static final String LOCALE = "locale";

    private static final Logger LOGGER = LogManager.getLogger(ShowEntityList.class);

    public static String showAuthorList(HttpServletRequest request, HttpServletResponse response) {
        AuthorService authorService = new AuthorService();

        final String PAGE_NUMBER = "pageNumber";
        final String PAGE_COUNT = "pageCount";
        final String PUBLICATION_USER_PAGE = "path.page.user.authorList";
        final String PUBLICATION_ADMIN_PAGE = "path.page.admin.authorList";
        final String PUBLICATION_LIST_ATTRIBUTE = "authors";

        String page;
        PageManager pageManager = PageManager.getInstance();
        Locale locale = (Locale)request.getSession().getAttribute(LOCALE);
        LocaleManager localeManager = new LocaleManager(locale);

        String pageNumber = request.getParameter(PAGE_NUMBER);
        pageNumber = (pageNumber == null) ? "1" : pageNumber;

        try {
            List<Author> authors = authorService.showAuthors(pageNumber);
            if (!authors.isEmpty()) {
                int pageCount = authorService.findAuthorPageCount();
                request.setAttribute(PUBLICATION_LIST_ATTRIBUTE, authors);
                request.setAttribute(PAGE_NUMBER, pageNumber);
                request.setAttribute(PAGE_COUNT, pageCount);
            } else {
                request.setAttribute(INFORMATION_IS_ABSENT_ATTRIBUTE, localeManager.getProperty(INFORMATION_IS_ABSENT_MESSAGE));
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

        final String PAGE_NUMBER = "pageNumber";
        final String PAGE_COUNT = "pageCount";
        final String PUBLICATION_USER_PAGE = "path.page.user.publicationList";
        final String PUBLICATION_ADMIN_PAGE = "path.page.admin.publicationList";
        final String PUBLICATION_LIST_ATTRIBUTE = "publications";

        String page;
        PageManager pageManager = PageManager.getInstance();
        Locale locale = (Locale)request.getSession().getAttribute(LOCALE);
        LocaleManager localeManager = new LocaleManager(locale);

        String pageNumber = request.getParameter(PAGE_NUMBER);
        pageNumber = (pageNumber == null) ? "1" : pageNumber;

        try {
            List<Publication> publications = publicationService.showPublications(pageNumber);
            if (!publications.isEmpty()) {
                int pageCount = publicationService.findPublicationPageCount();
                request.setAttribute(PUBLICATION_LIST_ATTRIBUTE, publications);
                request.setAttribute(PAGE_NUMBER, pageNumber);
                request.setAttribute(PAGE_COUNT, pageCount);
            } else {
                request.setAttribute(INFORMATION_IS_ABSENT_ATTRIBUTE, localeManager.getProperty(INFORMATION_IS_ABSENT_MESSAGE));
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
}
