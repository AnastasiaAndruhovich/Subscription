package by.andruhovich.subscription.command.genre;

import by.andruhovich.subscription.command.BaseCommand;
import by.andruhovich.subscription.command.CommandResult;
import by.andruhovich.subscription.command.TransitionType;
import by.andruhovich.subscription.entity.Genre;
import by.andruhovich.subscription.exception.MissingResourceTechnicalException;
import by.andruhovich.subscription.manager.PageManager;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Finds page according to relevant command parse genre entity
 */
public class ParseGenreCommand extends BaseCommand {
    private static final String EDIT_GENRE_ADMIN_PAGE = "path.page.admin.editGenre";

    private static final String GENRE_ID_ATTRIBUTE = "genreId";
    private static final String NAME_ATTRIBUTE = "name";
    private static final String DESCRIPTION_ATTRIBUTE = "description";
    private static final String GENRE_ATTRIBUTE = "genre";

    private static final Logger LOGGER = LogManager.getLogger(EditGenreCommand.class);

    /**
     * @param request http request
     * @param response http response
     * @return command result including page and transition type
     */
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        String page;
        PageManager pageManager = PageManager.getInstance();
        HttpSession session = request.getSession();

        String genreId = request.getParameter(GENRE_ID_ATTRIBUTE);
        String name = request.getParameter(NAME_ATTRIBUTE);
        String description = request.getParameter(DESCRIPTION_ATTRIBUTE);

        int id = Integer.parseInt(genreId);
        Genre genre = new Genre(id, name, description);
        session.setAttribute(GENRE_ATTRIBUTE, genre);

        try {
            page = pageManager.getProperty(EDIT_GENRE_ADMIN_PAGE);
        } catch (MissingResourceTechnicalException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
            page = ERROR_PAGE;
        }
        return new CommandResult(TransitionType.FORWARD, page);
    }
}
