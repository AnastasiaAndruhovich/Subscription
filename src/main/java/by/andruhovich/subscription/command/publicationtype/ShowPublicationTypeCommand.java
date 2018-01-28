package by.andruhovich.subscription.command.publicationtype;

import by.andruhovich.subscription.command.BaseCommand;
import by.andruhovich.subscription.entity.PublicationType;
import by.andruhovich.subscription.exception.MissingResourceTechnicalException;
import by.andruhovich.subscription.exception.ServiceTechnicalException;
import by.andruhovich.subscription.manager.ConfigurationManager;
import by.andruhovich.subscription.manager.MessageManager;
import by.andruhovich.subscription.service.PublicationTypeService;
import by.andruhovich.subscription.type.ClientType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

public class ShowPublicationTypeCommand implements BaseCommand{
    private PublicationTypeService publicationTypeService = new PublicationTypeService();

    private static final String PAGE_NUMBER = "pageNumber";
    private static final String PAGE_COUNT = "pageCount";
    private static final String PUBLICATION_TYPE_USER_PAGE = "path.page.user.publicationTypeList";
    private static final String PUBLICATION_TYPE_ADMIN_PAGE = "path.page.admin.publicationTypeList";
    private static final String PUBLICATION_LIST_ATTRIBUTE = "publicationTypes";
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

        try {
            List<PublicationType> publicationTypes = publicationTypeService.showPublicationTypes(pageNumber);
            if (!publicationTypes.isEmpty()) {
                int pageCount = publicationTypeService.findPublicationTypePageCount();
                request.setAttribute(PUBLICATION_LIST_ATTRIBUTE, publicationTypes);
                request.setAttribute(PAGE_NUMBER, pageNumber);
                request.setAttribute(PAGE_COUNT, pageCount);
            } else {
                request.setAttribute(INFORMATION_MESSAGE_ATTRIBUTE, messageManager.getProperty(PUBLICATION_MESSAGE));
            }

            HttpSession session = request.getSession();
            ClientType type = (ClientType) session.getAttribute(CLIENT_TYPE);
            if (type.equals(ClientType.ADMIN)) {
                page = configurationManager.getProperty(PUBLICATION_TYPE_ADMIN_PAGE);
            }
            else {
                page = configurationManager.getProperty(PUBLICATION_TYPE_USER_PAGE);
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
