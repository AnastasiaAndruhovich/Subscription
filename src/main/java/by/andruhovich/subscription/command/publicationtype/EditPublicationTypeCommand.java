package by.andruhovich.subscription.command.publicationtype;

import by.andruhovich.subscription.command.BaseCommand;
import by.andruhovich.subscription.command.CommandResult;
import by.andruhovich.subscription.command.TransitionType;
import by.andruhovich.subscription.entity.PublicationType;
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
 * Finds page according to relevant command edit publication type entity
 */
public class EditPublicationTypeCommand extends BaseCommand {
    private PublicationTypeService publicationTypeService = new PublicationTypeService();

    private static final String EDIT_PUBLICATION_TYPE_ADMIN_PAGE = "path.page.admin.editPublicationType";

    private static final String PUBLICATION_TYPE_ID_ATTRIBUTE = "publicationTypeId";
    private static final String NAME_ATTRIBUTE = "name";
    private static final String PUBLICATION_TYPE_ATTRIBUTE = "publicationType";

    private static final String SUCCESSFUL_EDIT_PUBLICATION_TYPE_MESSAGE = "message.successfulEditPublicationType";
    private static final String ERROR_EDIT_PUBLICATION_TYPE_MESSAGE = "message.errorEditPublicationType";

    private static final Logger LOGGER = LogManager.getLogger(EditPublicationTypeCommand.class);

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

        String publicationTypeId = request.getParameter(PUBLICATION_TYPE_ID_ATTRIBUTE);
        String name = request.getParameter(NAME_ATTRIBUTE);

        int id = Integer.parseInt(publicationTypeId);
        PublicationType publicationType = new PublicationType(id, name);
        session.setAttribute(PUBLICATION_TYPE_ATTRIBUTE, publicationType);

        try {
            if (!ServiceValidator.verifyName(name)) {
                page = pageManager.getProperty(EDIT_PUBLICATION_TYPE_ADMIN_PAGE);
                String errorNameMessage = localeManager.getProperty(ERROR_NAME_MESSAGE);
                session.setAttribute(MESSAGE_ATTRIBUTE, errorNameMessage);
                return new CommandResult(TransitionType.REDIRECT, page);
            }

            if (publicationTypeService.updatePublicationType(publicationTypeId, name)) {
                String successfulEditPublicationTypeMessage = localeManager.getProperty(SUCCESSFUL_EDIT_PUBLICATION_TYPE_MESSAGE);
                session.setAttribute(MESSAGE_ATTRIBUTE, successfulEditPublicationTypeMessage);
            } else {
                String errorEditPublicationTypeMessage = localeManager.getProperty(ERROR_EDIT_PUBLICATION_TYPE_MESSAGE);
                session.setAttribute(MESSAGE_ATTRIBUTE, errorEditPublicationTypeMessage);
            }
            page = pageManager.getProperty(EDIT_PUBLICATION_TYPE_ADMIN_PAGE);
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
