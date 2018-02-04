package by.andruhovich.subscription.command.author;

import by.andruhovich.subscription.command.BaseCommand;
import by.andruhovich.subscription.entity.Author;
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

public class EditAuthorCommand extends BaseCommand {
    private AuthorService authorService = new AuthorService();

    private static final String EDIT_AUTHOR_ADMIN_PAGE = "path.page.admin.editAuthor";

    private static final String AUTHOR_ID_ATTRIBUTE = "authorId";
    private static final String LAST_NAME_ATTRIBUTE = "lastName";
    private static final String FIRST_NAME_ATTRIBUTE = "firstName";
    private static final String PUBLISHER_NAME_ATTRIBUTE = "publisherName";
    private static final String AUTHOR_ATTRIBUTE = "author";
    private static final String RESULT_EDIT_AUTHOR_ATTRIBUTE = "result";

    private static final String SUCCESSFUL_EDIT_AUTHOR_MESSAGE = "message.successfulEditAuthor";
    private static final String ERROR_EDIT_AUTHOR_MESSAGE = "message.errorEditAuthor";

    private static final Logger LOGGER = LogManager.getLogger(EditAuthorCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String page;
        PageManager pageManager = PageManager.getInstance();
        Locale locale = (Locale) request.getSession().getAttribute(LOCALE);
        LocaleManager localeManager = new LocaleManager(locale);

        String authorId = request.getParameter(AUTHOR_ID_ATTRIBUTE);
        String lastName = request.getParameter(LAST_NAME_ATTRIBUTE);
        String firstName = request.getParameter(FIRST_NAME_ATTRIBUTE);
        String publisherName = request.getParameter(PUBLISHER_NAME_ATTRIBUTE);

        int id = Integer.parseInt(authorId);
        Author author = new Author(id, publisherName, lastName, firstName);
        request.setAttribute(AUTHOR_ATTRIBUTE, author);

        try {
            if (authorService.updateAuthor(authorId, lastName, firstName, publisherName)) {
                String successfulEditPublicationMessage = localeManager.getProperty(SUCCESSFUL_EDIT_AUTHOR_MESSAGE);
                request.setAttribute(RESULT_EDIT_AUTHOR_ATTRIBUTE, successfulEditPublicationMessage);
            } else {
                String errorEditPublicationMessage = localeManager.getProperty(ERROR_EDIT_AUTHOR_MESSAGE);
                request.setAttribute(RESULT_EDIT_AUTHOR_ATTRIBUTE, errorEditPublicationMessage);
            }
            page = pageManager.getProperty(EDIT_AUTHOR_ADMIN_PAGE);
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