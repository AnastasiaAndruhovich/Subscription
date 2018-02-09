package by.andruhovich.subscription.command.genre;

import by.andruhovich.subscription.command.BaseCommand;
import by.andruhovich.subscription.command.CommandResult;
import by.andruhovich.subscription.command.TransitionType;
import by.andruhovich.subscription.entity.Genre;
import by.andruhovich.subscription.exception.MissingResourceTechnicalException;
import by.andruhovich.subscription.exception.ServiceTechnicalException;
import by.andruhovich.subscription.manager.LocaleManager;
import by.andruhovich.subscription.manager.PageManager;
import by.andruhovich.subscription.service.GenreService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Locale;

public class EditGenreCommand extends BaseCommand {
    private GenreService genreService = new GenreService();

    private static final String EDIT_GENRE_ADMIN_PAGE = "path.page.admin.editGenre";

    private static final String GENRE_ID_ATTRIBUTE = "genreId";
    private static final String NAME_ATTRIBUTE = "name";
    private static final String DESCRIPTION_ATTRIBUTE = "description";
    private static final String GENRE_ATTRIBUTE = "genre";

    private static final String SUCCESSFUL_EDIT_GENRE_MESSAGE = "message.successfulEditGenre";
    private static final String ERROR_EDIT_GENRE_MESSAGE = "message.errorEditGenre";

    private static final Logger LOGGER = LogManager.getLogger(EditGenreCommand.class);

    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        String page;
        PageManager pageManager = PageManager.getInstance();
        Locale locale = (Locale) request.getSession().getAttribute(LOCALE);
        LocaleManager localeManager = new LocaleManager(locale);
        HttpSession session = request.getSession();
        session.removeAttribute(MESSAGE_ATTRIBUTE);

        String genreId = request.getParameter(GENRE_ID_ATTRIBUTE);
        String name = request.getParameter(NAME_ATTRIBUTE);
        String description = request.getParameter(DESCRIPTION_ATTRIBUTE);

        int id = Integer.parseInt(genreId);
        Genre genre = new Genre(id, name, description);
        session.setAttribute(GENRE_ATTRIBUTE, genre);

        try {
            if (genreService.updateGenre(genreId, name, description)) {
                String successfulEditGenreMessage = localeManager.getProperty(SUCCESSFUL_EDIT_GENRE_MESSAGE);
                session.setAttribute(MESSAGE_ATTRIBUTE, successfulEditGenreMessage);
            } else {
                String errorEditGenreMessage = localeManager.getProperty(ERROR_EDIT_GENRE_MESSAGE);
                session.setAttribute(MESSAGE_ATTRIBUTE, errorEditGenreMessage);
            }
            page = pageManager.getProperty(EDIT_GENRE_ADMIN_PAGE);
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
