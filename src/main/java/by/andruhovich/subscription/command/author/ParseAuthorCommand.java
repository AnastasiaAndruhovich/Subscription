package by.andruhovich.subscription.command.author;

import by.andruhovich.subscription.command.BaseCommand;
import by.andruhovich.subscription.entity.Author;
import by.andruhovich.subscription.exception.MissingResourceTechnicalException;
import by.andruhovich.subscription.manager.PageManager;
import by.andruhovich.subscription.service.AuthorService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ParseAuthorCommand extends BaseCommand {
    private static final String EDIT_AUTHOR_ADMIN_PAGE = "path.page.admin.editAuthor";

    private static final String AUTHOR_ID_ATTRIBUTE = "authorId";
    private static final String LAST_NAME_ATTRIBUTE = "lastName";
    private static final String FIRST_NAME_ATTRIBUTE = "firstName";
    private static final String PUBLISHER_NAME_ATTRIBUTE = "publisherName";
    private static final String AUTHOR_ATTRIBUTE = "author";

    private static final Logger LOGGER = LogManager.getLogger(EditAuthorCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String page;
        PageManager pageManager = PageManager.getInstance();

        String authorId = request.getParameter(AUTHOR_ID_ATTRIBUTE);
        String lastName = request.getParameter(LAST_NAME_ATTRIBUTE);
        String firstName = request.getParameter(FIRST_NAME_ATTRIBUTE);
        String publisherName = request.getParameter(PUBLISHER_NAME_ATTRIBUTE);

        int id = Integer.parseInt(authorId);
        Author author = new Author(id, publisherName, lastName, firstName);
        request.setAttribute(AUTHOR_ATTRIBUTE, author);

        try {
            page = pageManager.getProperty(EDIT_AUTHOR_ADMIN_PAGE);
        } catch (MissingResourceTechnicalException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
            page = ERROR_PAGE;
        }
        return page;
    }
}
