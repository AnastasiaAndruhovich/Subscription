package by.andruhovich.subscription.command.publicationtype;

import by.andruhovich.subscription.command.BaseCommand;
import by.andruhovich.subscription.entity.PublicationType;
import by.andruhovich.subscription.exception.MissingResourceTechnicalException;
import by.andruhovich.subscription.exception.ServiceTechnicalException;
import by.andruhovich.subscription.manager.PageManager;
import by.andruhovich.subscription.manager.LocaleManager;
import by.andruhovich.subscription.service.PublicationTypeService;
import by.andruhovich.subscription.type.ClientType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Locale;

public class ShowPublicationTypeCommand extends BaseCommand{
    private PublicationTypeService publicationTypeService = new PublicationTypeService();

    private static final String PAGE_NUMBER = "pageNumber";
    private static final String PAGE_COUNT = "pageCount";
    private static final String PUBLICATION_TYPE_USER_PAGE = "path.page.user.publicationTypeList";
    private static final String PUBLICATION_TYPE_ADMIN_PAGE = "path.page.admin.publicationTypeList";
    private static final String PUBLICATION_LIST_ATTRIBUTE = "publicationTypes";
    private static final String INFORMATION_MESSAGE_ATTRIBUTE = "informationIsAbsent";
    private static final String PUBLICATION_MESSAGE = "message.informationIsAbsent";
    private static final String CLIENT_TYPE = "clientType";
    private static final String LOCALE = "locale";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String page;
        PageManager pageManager = PageManager.getInstance();
        Locale locale = (Locale)request.getSession().getAttribute(LOCALE);
        LocaleManager localeManager = new LocaleManager(locale);

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
                request.setAttribute(INFORMATION_MESSAGE_ATTRIBUTE, localeManager.getProperty(PUBLICATION_MESSAGE));
            }

            HttpSession session = request.getSession();
            ClientType type = (ClientType) session.getAttribute(CLIENT_TYPE);
            if (type.equals(ClientType.ADMIN)) {
                page = pageManager.getProperty(PUBLICATION_TYPE_ADMIN_PAGE);
            }
            else {
                page = pageManager.getProperty(PUBLICATION_TYPE_USER_PAGE);
            }
        } catch (ServiceTechnicalException | MissingResourceTechnicalException e) {
            //log
            page = ERROR_PAGE;
        }
        return page;
    }
}
