package by.andruhovich.subscription.command.publicationtype;

import by.andruhovich.subscription.command.BaseCommand;
import by.andruhovich.subscription.command.CommandResult;
import by.andruhovich.subscription.command.TransitionType;
import by.andruhovich.subscription.exception.MissingResourceTechnicalException;
import by.andruhovich.subscription.exception.ServiceTechnicalException;
import by.andruhovich.subscription.manager.LocaleManager;
import by.andruhovich.subscription.manager.PageManager;
import by.andruhovich.subscription.service.PublicationTypeService;
import by.andruhovich.subscription.validator.ServiceValidator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Locale;

/**
 * Finds page according to relevant command add publication type entity
 */
public class AddPublicationTypeCommand extends BaseCommand {
    private PublicationTypeService publicationTypeService = new PublicationTypeService();

    private static final String ADD_PUBLICATION_TYPE_ADMIN_PAGE = "path.page.admin.addPublicationType";

    private static final String NAME_ATTRIBUTE = "name";

    private static final String SUCCESSFUL_ADD_PUBLICATION_TYPE_MESSAGE = "message.successfulAddPublicationType";
    private static final String ERROR_ADD_PUBLICATION_TYPE_MESSAGE = "message.errorAddPublicationType";

    private static final Logger LOGGER = LogManager.getLogger(AddPublicationTypeCommand.class);

    /**
     * @param request http request
     * @param response http response
     * @return command result including page and transition type
     */
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        String page;
        PageManager pageManager = PageManager.getInstance();
        Locale locale = (Locale) request.getSession().getAttribute(LOCALE);
        LocaleManager localeManager = new LocaleManager(locale);
        HttpSession session = request.getSession();
        session.removeAttribute(MESSAGE_ATTRIBUTE);

        String name = request.getParameter(NAME_ATTRIBUTE);

        try {
            if (!ServiceValidator.verifyName(name)) {
                page = pageManager.getProperty(ADD_PUBLICATION_TYPE_ADMIN_PAGE);
                String errorNameMessage = localeManager.getProperty(ERROR_NAME_MESSAGE);
                session.setAttribute(MESSAGE_ATTRIBUTE, errorNameMessage);
                return new CommandResult(TransitionType.REDIRECT, page);
            }

            int publicationTypeId = publicationTypeService.addPublicationType(name);
            if (publicationTypeId != -1) {
                String successfulAddedPublicationTypeMessage = localeManager.getProperty(SUCCESSFUL_ADD_PUBLICATION_TYPE_MESSAGE);
                session.setAttribute(MESSAGE_ATTRIBUTE, successfulAddedPublicationTypeMessage);
            } else {
                String errorAddedPublicationMessage = localeManager.getProperty(ERROR_ADD_PUBLICATION_TYPE_MESSAGE);
                session.setAttribute(MESSAGE_ATTRIBUTE, errorAddedPublicationMessage);
            }
            page = pageManager.getProperty(ADD_PUBLICATION_TYPE_ADMIN_PAGE);
        } catch (ServiceTechnicalException e) {
            LOGGER.log(Level.ERROR, "Database error connection");
            page = ERROR_PAGE;
        } catch (MissingResourceTechnicalException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
            page = ERROR_PAGE;
        }
        return new CommandResult(TransitionType.REDIRECT, page);
    }
}
