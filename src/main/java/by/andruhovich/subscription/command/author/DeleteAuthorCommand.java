package by.andruhovich.subscription.command.author;

import by.andruhovich.subscription.command.BaseCommand;
import by.andruhovich.subscription.command.CommandResult;
import by.andruhovich.subscription.command.TransitionType;
import by.andruhovich.subscription.command.common.ShowEntityList;
import by.andruhovich.subscription.exception.MissingResourceTechnicalException;
import by.andruhovich.subscription.exception.ServiceTechnicalException;
import by.andruhovich.subscription.manager.LocaleManager;
import by.andruhovich.subscription.service.AuthorService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Locale;

/**
 * Finds page according to relevant command delete author entity
 */
public class DeleteAuthorCommand extends BaseCommand {
    private AuthorService authorService = new AuthorService();

    private static final String AUTHOR_ID_ATTRIBUTE = "authorId";

    private static final String ERROR_DELETE_AUTHOR_MESSAGE = "message.errorDeleteAuthor";

    private static final Logger LOGGER = LogManager.getLogger(AddAuthorCommand.class);

    /**
     * @param request http request
     * @param response http response
     * @return command result including page and transition type
     */
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        Locale locale = (Locale) request.getSession().getAttribute(LOCALE);
        LocaleManager localeManager = new LocaleManager(locale);
        HttpSession session = request.getSession();
        session.removeAttribute(MESSAGE_ATTRIBUTE);

        String authorId = request.getParameter(AUTHOR_ID_ATTRIBUTE);

        try {
            if (!authorService.deleteAuthor(authorId)) {
                String errorDeleteAuthorMessage = localeManager.getProperty(ERROR_DELETE_AUTHOR_MESSAGE);
                session.setAttribute(MESSAGE_ATTRIBUTE, errorDeleteAuthorMessage);
            }
            return new CommandResult(TransitionType.REDIRECT, ShowEntityList.showAuthorList(request, response));
        } catch (ServiceTechnicalException e) {
            LOGGER.log(Level.ERROR, "Database error connection");
        } catch (MissingResourceTechnicalException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        return new CommandResult(TransitionType.REDIRECT, ERROR_PAGE);
    }
}
