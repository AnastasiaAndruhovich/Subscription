package by.andruhovich.subscription.command.publication;

import by.andruhovich.subscription.command.BaseCommand;
import by.andruhovich.subscription.command.CommandResult;
import by.andruhovich.subscription.command.TransitionType;
import by.andruhovich.subscription.entity.Publication;
import by.andruhovich.subscription.exception.MissingResourceTechnicalException;
import by.andruhovich.subscription.exception.ServiceTechnicalException;
import by.andruhovich.subscription.manager.PageManager;
import by.andruhovich.subscription.service.PublicationService;
import by.andruhovich.subscription.type.ClientType;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Finds page according to relevant command find publication entity by publication type id
 */
public class FindPublicationByPublicationTypeCommand extends BaseCommand {
    private PublicationService publicationService = new PublicationService();

    private static final String PAGE_NUMBER_ATTRIBUTE = "pageNumber";
    private static final String PAGE_COUNT_ATTRIBUTE = "pageCount";
    private static final String PUBLICATION_TYPE_ID_ATTRIBUTE = "publicationTypeId";
    private static final String PUBLICATION_LIST_ATTRIBUTE = "publications";

    private static final String PUBLICATION_USER_PAGE = "path.page.user.publicationByPublicationType";
    private static final String PUBLICATION_ADMIN_PAGE = "path.page.admin.publicationByPublicationType";

    private static final Logger LOGGER = LogManager.getLogger(FindPublicationByPublicationTypeCommand.class);

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
        session.removeAttribute(MESSAGE_ATTRIBUTE);

        String pageNumber = request.getParameter(PAGE_NUMBER_ATTRIBUTE);
        pageNumber = (pageNumber == null) ? "1" : pageNumber;
        String publicationTypeId = request.getParameter(PUBLICATION_TYPE_ID_ATTRIBUTE);
        session.setAttribute(PUBLICATION_TYPE_ID_ATTRIBUTE, publicationTypeId);

        try {
            List<Publication> publications = publicationService.findPublicationByPublicationTypeId(publicationTypeId, pageNumber);
            int pageCount = publicationService.findPublicationByPublicationTypeIdPageCount(publicationTypeId);
            session.setAttribute(PUBLICATION_LIST_ATTRIBUTE, publications);
            session.setAttribute(PAGE_NUMBER_ATTRIBUTE, pageNumber);
            session.setAttribute(PAGE_COUNT_ATTRIBUTE, pageCount);

            ClientType type = (ClientType) session.getAttribute(CLIENT_TYPE);
            if (type.equals(ClientType.ADMIN)) {
                page = pageManager.getProperty(PUBLICATION_ADMIN_PAGE);
            }
            else {
                page = pageManager.getProperty(PUBLICATION_USER_PAGE);
            }
        } catch (ServiceTechnicalException e) {
            LOGGER.log(Level.ERROR, "Database error connection");
            page = ERROR_PAGE;
        } catch (MissingResourceTechnicalException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
            page = ERROR_PAGE;
        }
        return new CommandResult(TransitionType.FORWARD, page);
    }
}
