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
    private static final String PAGE_NUMBER = "page_number";
    private static final String PUBLICATION_LIST_NAME = "publication_list";
    private static final String MAIN_PAGE = "path.main.page";

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String page = null;
        String pageNumber = request.getParameter(PAGE_NUMBER);
        int currentPageNumber = Integer.parseInt(pageNumber);
        ConfigurationManager configurationManager = ConfigurationManager.getInstance();
        MessageManager messageManager = MessageManager.getInstance();

        /*try {
            List<Publication> publications = publicationService.findPublications(currentPageNumber);
            if (!publications.isEmpty()) {
                request.setAttribute(PUBLICATION_LIST_NAME, publications);
                page = configurationManager.getProperty(MAIN_PAGE);
            } else {
                request.setAttribute("errorLoginPassMessage", messageManager.getProperty("message.error"));
                page = configurationManager.getProperty("path.page.login");
            }
        } catch (ServiceTechnicalException e) {
            //log
            page = configurationManager.getProperty("path.page.error");
        }*/
        return page;
    }
}
