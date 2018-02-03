package by.andruhovich.subscription.command.publication;

import by.andruhovich.subscription.command.BaseCommand;
import by.andruhovich.subscription.command.common.ShowEntityList;
import by.andruhovich.subscription.exception.MissingResourceTechnicalException;
import by.andruhovich.subscription.exception.ServiceTechnicalException;
import by.andruhovich.subscription.manager.LocaleManager;
import by.andruhovich.subscription.service.PublicationService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

public class DeletePublicationCommand extends BaseCommand {
    private PublicationService publicationService = new PublicationService();

    private static final String PUBLICATION_ID_ATTRIBUTE = "publicationId";
    private static final String RESULT_DELETE_PUBLICATION_ATTRIBUTE = "result";

    private static final String ERROR_DELETE_PUBLICATION_MESSAGE = "message.errorDeletePublication";

    private static final Logger LOGGER = LogManager.getLogger(DeletePublicationCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        Locale locale = (Locale) request.getSession().getAttribute(LOCALE);
        LocaleManager localeManager = new LocaleManager(locale);

        String publicationId = request.getParameter(PUBLICATION_ID_ATTRIBUTE);

        try {
            if (!publicationService.deletePublication(publicationId)) {
                String errorDeletePublicationMessage = localeManager.getProperty(ERROR_DELETE_PUBLICATION_MESSAGE);
                request.setAttribute(RESULT_DELETE_PUBLICATION_ATTRIBUTE, errorDeletePublicationMessage);
            }
            return ShowEntityList.showPublicationList(request, response);
        } catch (ServiceTechnicalException e) {
            LOGGER.log(Level.ERROR, "Database error connection");
            return ERROR_PAGE;
        } catch (MissingResourceTechnicalException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
            return ERROR_PAGE;
        }
    }
}