package by.andruhovich.subscription.command.user;

import by.andruhovich.subscription.command.BaseCommand;
import by.andruhovich.subscription.exception.ServiceTechnicalException;
import by.andruhovich.subscription.manager.ConfigurationManager;
import by.andruhovich.subscription.manager.MessageManager;
import by.andruhovich.subscription.service.UserService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginCommand implements BaseCommand {
    private UserService userService = new UserService();
    private static final String LOGIN = "login";
    private static final String PASSWORD = "password";

    private static final Logger LOGGER = LogManager.getLogger(LoginCommand.class);

    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String page;
        String login = request.getParameter(LOGIN);
        String password = request.getParameter(PASSWORD);
        ConfigurationManager configurationManager = ConfigurationManager.getInstance();
        MessageManager messageManager = MessageManager.getInstance();

        try {
            if (userService.checkLoginByPassword(login, password)) {
                request.setAttribute("user", login);
                page = configurationManager.getProperty("path.page.main");
            } else {
                request.setAttribute("errorLoginPassMessage", messageManager.getProperty("message.error"));
                page = configurationManager.getProperty("path.page.login");
            }
        } catch (ServiceTechnicalException e) {
            LOGGER.log(Level.ERROR, "Database error connection");
            page = configurationManager.getProperty("path.page.error");
        }
        return page;
    }
}
