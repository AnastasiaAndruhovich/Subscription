package by.andruhovich.subscription.command.publication;

import by.andruhovich.subscription.command.BaseCommand;
import by.andruhovich.subscription.entity.Publication;
import by.andruhovich.subscription.exception.ServiceTechnicalException;
import by.andruhovich.subscription.manager.ConfigurationManager;
import by.andruhovich.subscription.manager.MessageManager;
import by.andruhovich.subscription.service.PublicationService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class ShowPublicationCommand implements BaseCommand {
    private PublicationService publicationService = new PublicationService();

    private static final String PAGE_NUMBER = "pageNumber";
    private static final String PUBLICATION_PAGE = "path.page.publication";
    private static final String PUBLICATION_LIST_ATTRIBUTE = "publications";
    private static final String PUBLICATION_MESSAGE_ATTRIBUTE = "publicationIsAbsent";
    private static final String PUBLICATION_MESSAGE = "message.absent";
    private static final String ERROR_PAGE = "path.page.error";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String page;
        String pageNumber = request.getParameter(PAGE_NUMBER);
        ConfigurationManager configurationManager = ConfigurationManager.getInstance();
        MessageManager messageManager = MessageManager.getInstance();

        try {
            List<Publication> publications = publicationService.showPublications(pageNumber);
            if (!publications.isEmpty()) {
                request.setAttribute(PUBLICATION_LIST_ATTRIBUTE, publications);
            } else {
                request.setAttribute(PUBLICATION_MESSAGE_ATTRIBUTE, "Publications" + messageManager.getProperty(PUBLICATION_MESSAGE));
            }
            page = configurationManager.getProperty(PUBLICATION_PAGE);
        } catch (ServiceTechnicalException e) {
            //log
            page = configurationManager.getProperty(ERROR_PAGE);
        }
        return page;
    }
}
