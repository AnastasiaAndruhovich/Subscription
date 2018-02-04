package by.andruhovich.subscription.command.publication;

import by.andruhovich.subscription.command.BaseCommand;
import by.andruhovich.subscription.entity.Publication;
import by.andruhovich.subscription.exception.MissingResourceTechnicalException;
import by.andruhovich.subscription.exception.ServiceTechnicalException;
import by.andruhovich.subscription.manager.PageManager;
import by.andruhovich.subscription.manager.LocaleManager;
import by.andruhovich.subscription.service.PublicationService;
import by.andruhovich.subscription.type.ClientType;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Locale;

public class FindPublicationByPublicationTypeCommand extends BaseCommand {
    private PublicationService publicationService = new PublicationService();

    private static final String PAGE_NUMBER_ATTRIBUTE = "pageNumber";
    private static final String PAGE_COUNT_ATTRIBUTE = "pageCount";
    private static final String PUBLICATION_TYPE_ID_ATTRIBUTE = "publicationTypeId";
    private static final String PUBLICATION_LIST_ATTRIBUTE = "publications";

    private static final String PUBLICATION_USER_PAGE = "path.page.user.publicationList";
    private static final String PUBLICATION_ADMIN_PAGE = "path.page.admin.publicationList";

    private static final Logger LOGGER = LogManager.getLogger(FindPublicationByPublicationTypeCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String page;
        PageManager pageManager = PageManager.getInstance();

        String pageNumber = request.getParameter(PAGE_NUMBER_ATTRIBUTE);
        pageNumber = (pageNumber == null) ? "1" : pageNumber;
        String publicationTypeId = request.getParameter(PUBLICATION_TYPE_ID_ATTRIBUTE);

        try {
            List<Publication> publications = publicationService.findPublicationByPublicationTypeId(publicationTypeId, pageNumber);
            if (!publications.isEmpty()) {
                int pageCount = publicationService.findPublicationPageCount();
                request.setAttribute(PUBLICATION_LIST_ATTRIBUTE, publications);
                request.setAttribute(PAGE_NUMBER_ATTRIBUTE, pageNumber);
                request.setAttribute(PAGE_COUNT_ATTRIBUTE, pageCount);
            }

            HttpSession session = request.getSession();
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
        return page;
    }
}
