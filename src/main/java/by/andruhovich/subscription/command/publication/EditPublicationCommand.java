package by.andruhovich.subscription.command.publication;

import by.andruhovich.subscription.command.BaseCommand;
import by.andruhovich.subscription.command.CommandResult;
import by.andruhovich.subscription.command.TransitionType;
import by.andruhovich.subscription.entity.Publication;
import by.andruhovich.subscription.exception.MissingResourceTechnicalException;
import by.andruhovich.subscription.exception.ServiceTechnicalException;
import by.andruhovich.subscription.manager.LocaleManager;
import by.andruhovich.subscription.manager.PageManager;
import by.andruhovich.subscription.service.PublicationService;
import by.andruhovich.subscription.validator.ServiceValidator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class EditPublicationCommand extends BaseCommand {
    private PublicationService publicationService = new PublicationService();

    private static final String EDIT_PUBLICATION_ADMIN_PAGE = "path.page.admin.editPublication";
    private static final String ADD_PUBLICATION_PICTURE_ADMIN_PAGE = "path.page.admin.addPublicationPicture";

    private static final String PUBLICATION_ID_ATTRIBUTE = "publicationId";
    private static final String NAME_ATTRIBUTE = "name";
    private static final String PUBLICATION_TYPE_ATTRIBUTE = "publicationType";
    private static final String GENRE_ATTRIBUTE = "genre";
    private static final String LAST_NAME_ATTRIBUTE = "lastName";
    private static final String FIRST_NAME_ATTRIBUTE = "firstName";
    private static final String PUBLISHER_NAME_ATTRIBUTE = "publisherName";
    private static final String DESCRIPTION_ATTRIBUTE = "description";
    private static final String PRICE_ATTRIBUTE = "price";
    private static final String PUBLICATION_ATTRIBUTE = "publication";

    private static final String SUCCESSFUL_EDIT_PUBLICATION_MESSAGE = "message.successfulEditPublication";
    private static final String ERROR_EDIT_PUBLICATION_MESSAGE = "message.errorEditPublication";
    private static final String INCORRECT_PRICE_MESSAGE = "message.incorrectMoneyFormat";

    private static final Logger LOGGER = LogManager.getLogger(EditPublicationCommand.class);

    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        String page;
        PageManager pageManager = PageManager.getInstance();
        Locale locale = (Locale)request.getSession().getAttribute(LOCALE);
        LocaleManager localeManager = new LocaleManager(locale);

        String publicationId = request.getParameter(PUBLICATION_ID_ATTRIBUTE);
        String name = request.getParameter(NAME_ATTRIBUTE);
        String publicationType = request.getParameter(PUBLICATION_TYPE_ATTRIBUTE);
        String genre = request.getParameter(GENRE_ATTRIBUTE);
        String lastName = request.getParameter(LAST_NAME_ATTRIBUTE);
        String firstName = request.getParameter(FIRST_NAME_ATTRIBUTE);
        String publisherName = request.getParameter(PUBLISHER_NAME_ATTRIBUTE);
        String description = request.getParameter(DESCRIPTION_ATTRIBUTE);
        String price = request.getParameter(PRICE_ATTRIBUTE);

        List<String> lastNames = new LinkedList<>();
        lastNames.add(lastName);
        List<String> firstNames = new LinkedList<>();
        firstNames.add(firstName);

        try {
            if (!ServiceValidator.verifyPrice(price)) {
                String incorrectPriceMessage = localeManager.getProperty(INCORRECT_PRICE_MESSAGE);
                request.setAttribute(MESSAGE_ATTRIBUTE, incorrectPriceMessage);
                page = pageManager.getProperty(EDIT_PUBLICATION_ADMIN_PAGE);
                return new CommandResult(TransitionType.REDIRECT, page);
            }

            if (publicationService.updatePublication(publicationId, name, firstNames, lastNames, publisherName, publicationType,
                    genre, description, price)) {
                String successfulEditPublicationMessage = localeManager.getProperty(SUCCESSFUL_EDIT_PUBLICATION_MESSAGE);
                request.setAttribute(MESSAGE_ATTRIBUTE, successfulEditPublicationMessage);
                int id = Integer.parseInt(publicationId);
                Publication publication = publicationService.findPublicationById(id);
                request.setAttribute(PUBLICATION_ATTRIBUTE, publication);
                page = pageManager.getProperty(ADD_PUBLICATION_PICTURE_ADMIN_PAGE);
            } else {
                String errorAddedPublicationMessage = localeManager.getProperty(ERROR_EDIT_PUBLICATION_MESSAGE);
                request.setAttribute(MESSAGE_ATTRIBUTE, errorAddedPublicationMessage);
                page = pageManager.getProperty(EDIT_PUBLICATION_ADMIN_PAGE);
            }

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
