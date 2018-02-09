package by.andruhovich.subscription.command.user;

import by.andruhovich.subscription.command.BaseCommand;
import by.andruhovich.subscription.command.CommandResult;
import by.andruhovich.subscription.command.TransitionType;
import by.andruhovich.subscription.command.common.ShowEntityList;
import by.andruhovich.subscription.entity.Role;
import by.andruhovich.subscription.exception.MissingResourceTechnicalException;
import by.andruhovich.subscription.exception.ServiceTechnicalException;
import by.andruhovich.subscription.manager.LocaleManager;
import by.andruhovich.subscription.manager.PageManager;
import by.andruhovich.subscription.service.UserService;
import by.andruhovich.subscription.type.ClientType;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Locale;

public class LoginCommand extends BaseCommand {
    private UserService userService = new UserService();

    private static final String LOGIN = "login";
    private static final String PASSWORD = "password";

    private static final String ERROR_LOGIN_MESSAGE = "message.errorLogin";

    private static final String LOGIN_PAGE = "path.page.user.login";

    private static final Logger LOGGER = LogManager.getLogger(LoginCommand.class);

    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        String page;
        String login = request.getParameter(LOGIN);
        String password = request.getParameter(PASSWORD);
        PageManager pageManager = PageManager.getInstance();
        Locale locale = (Locale)request.getSession().getAttribute(LOCALE);
        LocaleManager localeManager = new LocaleManager(locale);
        HttpSession session = request.getSession();
        session.removeAttribute(MESSAGE_ATTRIBUTE);

        try {
            Integer id = userService.findUserIdByLogin(login);
            if (userService.confirmPassword(id.toString(), password)) {
                int clientId = userService.findUserIdByLogin(login);
                session.setAttribute(CLIENT_ID, clientId);
                Role role = userService.findRoleByUserId(clientId);
                session.setAttribute(CLIENT_TYPE, ClientType.valueOf(role.getName().toUpperCase()));
                page = ShowEntityList.showPublicationList(request, response);
            } else {
                String errorLoginMessage = localeManager.getProperty(ERROR_LOGIN_MESSAGE);
                session.setAttribute(MESSAGE_ATTRIBUTE, errorLoginMessage);
                page = pageManager.getProperty(LOGIN_PAGE);
            }
        } catch (ServiceTechnicalException e) {
            LOGGER.log(Level.ERROR, "Database error connection");
            page = ERROR_PAGE;
        } catch (MissingResourceTechnicalException e) {
            page = ERROR_PAGE;
        }
        return new CommandResult(TransitionType.FORWARD, page);
    }
}
