package by.andruhovich.subscription.command.user;

import by.andruhovich.subscription.command.BaseCommand;
import by.andruhovich.subscription.command.CommandResult;
import by.andruhovich.subscription.command.TransitionType;
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
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Finds page according to relevant command find blocked entity user by admin id
 */
public class FindBlockedUserByAdminCommand extends BaseCommand {
    private UserService userService = new UserService();

    private static final String USER_LIST_ATTRIBUTE = "users";
    private static final String ADMIN_ID_ATTRIBUTE = "adminId";
    private static final String USER_BY_ADMIN_PAGE = "path.page.admin.userByAdmin";
    private static final String PAGE_NUMBER_ATTRIBUTE = "pageNumber";

    private static final Logger LOGGER = LogManager.getLogger(FindBlockedUserByAdminCommand.class);

    /**
     * @param request http request
     * @param response http response
     * @return command result including page and transition type
     */
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        String page;
        PageManager pageManager = PageManager.getInstance();
        HttpSession session = request.getSession();

        String adminId = request.getParameter(ADMIN_ID_ATTRIBUTE);
        session.setAttribute(ADMIN_ID_ATTRIBUTE, adminId);
        String pageNumber = request.getParameter(PAGE_NUMBER_ATTRIBUTE);
        pageNumber = (pageNumber == null) ? "1" : pageNumber;

        try {
            List<User> users = userService.findBlockedUsersByAdminId(adminId, pageNumber);
            session.setAttribute(USER_LIST_ATTRIBUTE, users);
            page = pageManager.getProperty(USER_BY_ADMIN_PAGE);
        } catch (ServiceTechnicalException e) {
            LOGGER.log(Level.ERROR, "Database error connection");
            page = ERROR_PAGE;
        } catch (MissingResourceTechnicalException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
            page = ERROR_PAGE;
        }
        return new CommandResult(TransitionType.FORWARD, page);
    }
}
