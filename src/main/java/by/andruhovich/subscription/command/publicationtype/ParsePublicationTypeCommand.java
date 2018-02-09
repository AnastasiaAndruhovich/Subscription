package by.andruhovich.subscription.command.publicationtype;

import by.andruhovich.subscription.command.BaseCommand;
import by.andruhovich.subscription.command.CommandResult;
import by.andruhovich.subscription.command.TransitionType;
import by.andruhovich.subscription.entity.PublicationType;
import by.andruhovich.subscription.exception.MissingResourceTechnicalException;
import by.andruhovich.subscription.manager.PageManager;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ParsePublicationTypeCommand extends BaseCommand {
    private static final String EDIT_PUBLICATION_TYPE_ADMIN_PAGE = "path.page.admin.editPublicationType";

    private static final String PUBLICATION_TYPE_ID_ATTRIBUTE = "publicationTypeId";
    private static final String NAME_ATTRIBUTE = "name";
    private static final String PUBLICATION_TYPE_ATTRIBUTE = "publicationType";

    private static final Logger LOGGER = LogManager.getLogger(EditPublicationTypeCommand.class);

    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        String page;
        PageManager pageManager = PageManager.getInstance();
        HttpSession session = request.getSession();

        String genreId = request.getParameter(PUBLICATION_TYPE_ID_ATTRIBUTE);
        String name = request.getParameter(NAME_ATTRIBUTE);

        int id = Integer.parseInt(genreId);
        PublicationType publicationType = new PublicationType(id, name);
        session.setAttribute(PUBLICATION_TYPE_ATTRIBUTE, publicationType);

        try {
            page = pageManager.getProperty(EDIT_PUBLICATION_TYPE_ADMIN_PAGE);
        } catch (MissingResourceTechnicalException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
            page = ERROR_PAGE;
        }
        return new CommandResult(TransitionType.FORWARD, page);
    }
}
