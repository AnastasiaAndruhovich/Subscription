package by.andruhovich.subscription.command.publicationtype;

import by.andruhovich.subscription.command.BaseCommand;
import by.andruhovich.subscription.command.common.ShowEntityList;
import by.andruhovich.subscription.command.genre.AddGenreCommand;
import by.andruhovich.subscription.exception.MissingResourceTechnicalException;
import by.andruhovich.subscription.exception.ServiceTechnicalException;
import by.andruhovich.subscription.manager.LocaleManager;
import by.andruhovich.subscription.service.PublicationTypeService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

public class DeletePublicationTypeCommand extends BaseCommand {
    private PublicationTypeService publicationTypeService = new PublicationTypeService();

    private static final String PUBLICATION_TYPE_ID_ATTRIBUTE = "publicationTypeId";

    private static final String ERROR_DELETE_PUBLICATION_TYPE_MESSAGE = "message.errorDeletePublicationType";

    private static final Logger LOGGER = LogManager.getLogger(AddGenreCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        Locale locale = (Locale) request.getSession().getAttribute(LOCALE);
        LocaleManager localeManager = new LocaleManager(locale);

        String genreId = request.getParameter(PUBLICATION_TYPE_ID_ATTRIBUTE);

        try {
            if (!publicationTypeService.deletePublicationType(genreId)) {
                String errorDeleteAuthorMessage = localeManager.getProperty(ERROR_DELETE_PUBLICATION_TYPE_MESSAGE);
                request.setAttribute(MESSAGE_ATTRIBUTE, errorDeleteAuthorMessage);
            }
            return ShowEntityList.showPublicationTypeList(request, response);
        } catch (ServiceTechnicalException e) {
            LOGGER.log(Level.ERROR, "Database error connection");
            return ERROR_PAGE;
        } catch (MissingResourceTechnicalException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
            return ERROR_PAGE;
        }
    }
}
