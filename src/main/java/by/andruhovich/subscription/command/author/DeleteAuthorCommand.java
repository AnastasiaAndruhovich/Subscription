package by.andruhovich.subscription.command.author;

import by.andruhovich.subscription.command.BaseCommand;
import by.andruhovich.subscription.command.common.ShowEntityList;
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

public class DeleteAuthorCommand extends BaseCommand {
    private AuthorService authorService = new AuthorService();

    private static final String AUTHOR_ID_ATTRIBUTE = "authorId";
    private static final String RESULT_DELETE_AUTHOR_ATTRIBUTE = "result";

    private static final String ERROR_DELETE_AUTHOR_MESSAGE = "message.errorDeleteAuthor";

    private static final Logger LOGGER = LogManager.getLogger(AddAuthorCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        Locale locale = (Locale) request.getSession().getAttribute(LOCALE);
        LocaleManager localeManager = new LocaleManager(locale);

        String authorId = request.getParameter(AUTHOR_ID_ATTRIBUTE);

        try {
            if (!authorService.deleteAuthor(authorId)) {
                String errorDeleteAuthorMessage = localeManager.getProperty(ERROR_DELETE_AUTHOR_MESSAGE);
                request.setAttribute(RESULT_DELETE_AUTHOR_ATTRIBUTE, errorDeleteAuthorMessage);
            }
            return ShowEntityList.showAuthorList(request, response);
        } catch (ServiceTechnicalException e) {
            LOGGER.log(Level.ERROR, "Database error connection");
            return ERROR_PAGE;
        } catch (MissingResourceTechnicalException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
            return ERROR_PAGE;
        }
    }
}
