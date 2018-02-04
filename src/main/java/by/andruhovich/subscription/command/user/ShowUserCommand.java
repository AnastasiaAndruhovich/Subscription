package by.andruhovich.subscription.command.user;

import by.andruhovich.subscription.command.BaseCommand;
import by.andruhovich.subscription.entity.User;
import by.andruhovich.subscription.exception.MissingResourceTechnicalException;
import by.andruhovich.subscription.exception.ServiceTechnicalException;
import by.andruhovich.subscription.manager.PageManager;
import by.andruhovich.subscription.service.UserService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class ShowUserCommand extends BaseCommand {
    private static final Logger LOGGER = LogManager.getLogger(ShowUserCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        UserService userService = new UserService();

        final String PUBLICATION_ADMIN_PAGE = "path.page.admin.userList";
        final String PUBLICATION_LIST_ATTRIBUTE = "users";

        final String PAGE_NUMBER = "pageNumber";
        final String PAGE_COUNT = "pageCount";

        String page;
        PageManager pageManager = PageManager.getInstance();

        String pageNumber = request.getParameter(PAGE_NUMBER);
        pageNumber = (pageNumber == null) ? "1" : pageNumber;

        try {
            List<User> users = userService.showUsers(pageNumber);
            if (!users.isEmpty()) {
                int pageCount = userService.findUserPageCount();
                request.setAttribute(PUBLICATION_LIST_ATTRIBUTE, users);
                request.setAttribute(PAGE_NUMBER, pageNumber);
                request.setAttribute(PAGE_COUNT, pageCount);
            }
            page = pageManager.getProperty(PUBLICATION_ADMIN_PAGE);

        } catch (ServiceTechnicalException e) {
            LOGGER.log(Level.ERROR, "Database error connection");
            page = ERROR_PAGE;
        } catch (MissingResourceTechnicalException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
            page = ERROR_PAGE;
        }
        return page;
    }
}
