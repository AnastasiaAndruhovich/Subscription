package by.andruhovich.subscription.command.genre;

import by.andruhovich.subscription.command.BaseCommand;
import by.andruhovich.subscription.command.CommandResult;
import by.andruhovich.subscription.command.TransitionType;
import by.andruhovich.subscription.command.common.ShowEntityList;
import by.andruhovich.subscription.exception.MissingResourceTechnicalException;
import by.andruhovich.subscription.exception.ServiceTechnicalException;
import by.andruhovich.subscription.manager.LocaleManager;
import by.andruhovich.subscription.service.GenreService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Locale;

/**
 * Finds page according to relevant command delete genre entity
 */
public class DeleteGenreCommand extends BaseCommand {
    private GenreService genreService = new GenreService();

    private static final String GENRE_ID_ATTRIBUTE = "genreId";

    private static final String ERROR_DELETE_GENRE_MESSAGE = "message.errorDeleteGenre";

    private static final Logger LOGGER = LogManager.getLogger(AddGenreCommand.class);

    /**
     * @param request http request
     * @param response http response
     * @return command result including page and transition type
     */
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        Locale locale = (Locale) request.getSession().getAttribute(LOCALE);
        LocaleManager localeManager = new LocaleManager(locale);
        String page;
        HttpSession session = request.getSession();
        session.removeAttribute(MESSAGE_ATTRIBUTE);

        String genreId = request.getParameter(GENRE_ID_ATTRIBUTE);

        try {
            if (!genreService.deleteGenre(genreId)) {
                String errorDeleteAuthorMessage = localeManager.getProperty(ERROR_DELETE_GENRE_MESSAGE);
                session.setAttribute(MESSAGE_ATTRIBUTE, errorDeleteAuthorMessage);
            }
            page = ShowEntityList.showGenreList(request, response);
        } catch (ServiceTechnicalException e) {
            LOGGER.log(Level.ERROR, "Database error connection");
            page = ERROR_PAGE;
        } catch (MissingResourceTechnicalException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
            page = ERROR_PAGE;
        }
        return new CommandResult(TransitionType.REDIRECT, page);
    }
}
