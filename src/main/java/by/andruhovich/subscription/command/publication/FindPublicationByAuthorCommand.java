package by.andruhovich.subscription.command.publication;

import by.andruhovich.subscription.command.BaseCommand;
import by.andruhovich.subscription.entity.Publication;
import by.andruhovich.subscription.exception.MissingResourceTechnicalException;
import by.andruhovich.subscription.exception.ServiceTechnicalException;
import by.andruhovich.subscription.manager.ConfigurationManager;
import by.andruhovich.subscription.manager.MessageManager;
import by.andruhovich.subscription.service.PublicationService;
import by.andruhovich.subscription.type.ClientType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

public class FindPublicationByAuthorCommand implements BaseCommand{
    private PublicationService publicationService = new PublicationService();

    private static final String PAGE_NUMBER = "pageNumber";
    private static final String PAGE_COUNT = "pageCount";
    private static final String AUTHOR_ID = "authorId";
    private static final String PUBLICATION_USER_PAGE = "path.page.user.publicationList";
    private static final String PUBLICATION_ADMIN_PAGE = "path.page.admin.publicationList";
    private static final String PUBLICATION_LIST_ATTRIBUTE = "publications";
    private static final String INFORMATION_MESSAGE_ATTRIBUTE = "informationIsAbsent";
    private static final String PUBLICATION_MESSAGE = "message.informationIsAbsent";
    private static final String ERROR_PAGE = "path.page.error";
    private static final String CLIENT_TYPE = "clientType";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String page;
        ConfigurationManager configurationManager = ConfigurationManager.getInstance();
        MessageManager messageManager = MessageManager.getInstance();

        String pageNumber = request.getParameter(PAGE_NUMBER);
        pageNumber = (pageNumber == null) ? "1" : pageNumber;
        String authorId = request.getParameter(AUTHOR_ID);

        try {
            List<Publication> publications = publicationService.findPublicationByAuthorId(authorId);
            if (!publications.isEmpty()) {
                //int pageCount = publicationService.findPublicationPageCount();
                request.setAttribute(PUBLICATION_LIST_ATTRIBUTE, publications);
                /*request.setAttribute(PAGE_NUMBER, pageNumber);
                request.setAttribute(PAGE_COUNT, pageCount);*/
            } else {
                request.setAttribute(INFORMATION_MESSAGE_ATTRIBUTE, messageManager.getProperty(PUBLICATION_MESSAGE));
            }

            HttpSession session = request.getSession();
            ClientType type = (ClientType) session.getAttribute(CLIENT_TYPE);
            if (type.equals(ClientType.ADMIN)) {
                page = configurationManager.getProperty(PUBLICATION_ADMIN_PAGE);
            }
            else {
                page = configurationManager.getProperty(PUBLICATION_USER_PAGE);
            }
        } catch (ServiceTechnicalException | MissingResourceTechnicalException e) {
            //log
            try {
                page = configurationManager.getProperty(ERROR_PAGE);
            } catch (MissingResourceTechnicalException e1) {
                //?????
                page = null;
            }
        }
        return page;
    }
}
