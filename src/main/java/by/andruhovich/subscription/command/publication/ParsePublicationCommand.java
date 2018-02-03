package by.andruhovich.subscription.command.publication;

import by.andruhovich.subscription.command.BaseCommand;
import by.andruhovich.subscription.entity.Publication;
import by.andruhovich.subscription.exception.MissingResourceTechnicalException;
import by.andruhovich.subscription.exception.ServiceTechnicalException;
import by.andruhovich.subscription.manager.PageManager;
import by.andruhovich.subscription.service.PublicationService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ParsePublicationCommand extends BaseCommand{
    private PublicationService publicationService = new PublicationService();
    private static final String EDIT_PUBLICATION_ADMIN_PAGE = "path.page.admin.editPublication";

    private static final String PUBLICATION_ID_ATTRIBUTE = "publicationId";
    private static final String PUBLICATION_ATTRIBUTE = "publication";

    private static final Logger LOGGER = LogManager.getLogger(ParsePublicationCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String page;
        PageManager pageManager = PageManager.getInstance();

        String publicationId = request.getParameter(PUBLICATION_ID_ATTRIBUTE);

        try {
            int id = Integer.parseInt(publicationId);
            Publication publication = publicationService.findPublicationById(id);
            request.setAttribute(PUBLICATION_ATTRIBUTE, publication);
            page = pageManager.getProperty(EDIT_PUBLICATION_ADMIN_PAGE);
        } catch (MissingResourceTechnicalException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
            page = ERROR_PAGE;
        } catch (ServiceTechnicalException e) {
            LOGGER.log(Level.ERROR, "Database error connection");
            page = ERROR_PAGE;
        }
        return page;
    }
}
