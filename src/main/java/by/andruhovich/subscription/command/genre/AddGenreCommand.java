package by.andruhovich.subscription.command.genre;

import by.andruhovich.subscription.command.BaseCommand;
import by.andruhovich.subscription.command.CommandResult;
import by.andruhovich.subscription.command.TransitionType;
import by.andruhovich.subscription.exception.MissingResourceTechnicalException;
import by.andruhovich.subscription.exception.ServiceTechnicalException;
import by.andruhovich.subscription.manager.LocaleManager;
import by.andruhovich.subscription.manager.PageManager;
import by.andruhovich.subscription.service.GenreService;
import by.andruhovich.subscription.validator.ServiceValidator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Locale;

public class AddGenreCommand extends BaseCommand {
    private GenreService genreService = new GenreService();

    private static final String ADD_GENRE_ADMIN_PAGE = "path.page.admin.addGenre";

    private static final String NAME_ATTRIBUTE = "name";
    private static final String DESCRIPTION_ATTRIBUTE = "description";

    private static final String SUCCESSFUL_ADD_GENRE_MESSAGE = "message.successfulAddGenre";
    private static final String ERROR_ADD_GENRE_MESSAGE = "message.errorAddGenre";

    private static final Logger LOGGER = LogManager.getLogger(AddGenreCommand.class);

    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        String page;
        PageManager pageManager = PageManager.getInstance();
        Locale locale = (Locale) request.getSession().getAttribute(LOCALE);
        LocaleManager localeManager = new LocaleManager(locale);
        HttpSession session = request.getSession();
        session.removeAttribute(MESSAGE_ATTRIBUTE);

        String name = request.getParameter(NAME_ATTRIBUTE);
        String description = request.getParameter(DESCRIPTION_ATTRIBUTE);

        try {
            if (!ServiceValidator.verifyName(name)) {
                page = pageManager.getProperty(ADD_GENRE_ADMIN_PAGE);
                String errorNameMessage = localeManager.getProperty(ERROR_NAME_MESSAGE);
                session.setAttribute(MESSAGE_ATTRIBUTE, errorNameMessage);
                return new CommandResult(TransitionType.REDIRECT, page);
            }

            int genreId = genreService.addGenre(name, description);
            if (genreId != -1) {
                String successfulAddedPublicationMessage = localeManager.getProperty(SUCCESSFUL_ADD_GENRE_MESSAGE);
                session.setAttribute(MESSAGE_ATTRIBUTE, successfulAddedPublicationMessage);
            } else {
                String errorAddedPublicationMessage = localeManager.getProperty(ERROR_ADD_GENRE_MESSAGE);
                session.setAttribute(MESSAGE_ATTRIBUTE, errorAddedPublicationMessage);
            }
            page = pageManager.getProperty(ADD_GENRE_ADMIN_PAGE);
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
