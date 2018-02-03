package by.andruhovich.subscription.command.author;

import by.andruhovich.subscription.command.BaseCommand;
import by.andruhovich.subscription.command.genre.AddGenreCommand;
import by.andruhovich.subscription.exception.MissingResourceTechnicalException;
import by.andruhovich.subscription.exception.ServiceTechnicalException;
import by.andruhovich.subscription.manager.LocaleManager;
import by.andruhovich.subscription.manager.PageManager;
import by.andruhovich.subscription.service.AuthorService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

public class AddAuthorCommand extends BaseCommand {
    private AuthorService authorService = new AuthorService();

    private static final String ADD_AUTHOR_ADMIN_PAGE = "path.page.admin.addAuthor";

    private static final String LAST_NAME_ATTRIBUTE = "lastName";
    private static final String FIRST_NAME_ATTRIBUTE = "firstName";
    private static final String PUBLISHER_NAME_ATTRIBUTE = "publisherName";
    private static final String RESULT_ADD_AUTHOR_ATTRIBUTE = "result";

    private static final String SUCCESSFUL_ADD_AUTHOR_MESSAGE = "message.successfulAddAuthor";
    private static final String ERROR_ADD_AUTHOR_MESSAGE = "message.errorAddAuthor";

    private static final Logger LOGGER = LogManager.getLogger(AddAuthorCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String page;
        PageManager pageManager = PageManager.getInstance();
        Locale locale = (Locale) request.getSession().getAttribute(LOCALE);
        LocaleManager localeManager = new LocaleManager(locale);

        String lastName = request.getParameter(LAST_NAME_ATTRIBUTE);
        String firstName = request.getParameter(FIRST_NAME_ATTRIBUTE);
        String publisherName = request.getParameter(PUBLISHER_NAME_ATTRIBUTE);

        try {
            int authorId = authorService.addAuthor(firstName, lastName, publisherName);
            if (authorId != -1) {
                String successfulAddedPublicationMessage = localeManager.getProperty(SUCCESSFUL_ADD_AUTHOR_MESSAGE);
                request.setAttribute(RESULT_ADD_AUTHOR_ATTRIBUTE, successfulAddedPublicationMessage);
            } else {
                String errorAddedPublicationMessage = localeManager.getProperty(ERROR_ADD_AUTHOR_MESSAGE);
                request.setAttribute(RESULT_ADD_AUTHOR_ATTRIBUTE, errorAddedPublicationMessage);
            }
            page = pageManager.getProperty(ADD_AUTHOR_ADMIN_PAGE);
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
