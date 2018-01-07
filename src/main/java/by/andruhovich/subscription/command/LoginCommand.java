package by.andruhovich.subscription.command;

import by.andruhovich.subscription.exception.ServiceTechnicalException;
import by.andruhovich.subscription.manager.ConfigurationManager;
import by.andruhovich.subscription.manager.MessageManager;
import by.andruhovich.subscription.service.UserService;

import javax.servlet.http.HttpServletRequest;

public class LoginCommand implements BaseCommand{
    private UserService userService = new UserService();
    private static final String LOGIN = "login";
    private static final String PASSWORD = "password";

    public String execute(HttpServletRequest request) {
        String page;
        String login = request.getParameter(LOGIN);
        String password = request.getParameter(PASSWORD);

        try {
            if (userService.checkLogin(login, password)) {
                request.setAttribute("user", login);
                page = ConfigurationManager.getProperty("path.page.main");
            } else {
                request.setAttribute("errorLoginPassMessage", MessageManager.getProperty("message.error"));
                page = ConfigurationManager.getProperty("path.page.login");
            }
        } catch (ServiceTechnicalException e) {
            //log
            page = ConfigurationManager.getProperty("path.page.error");
        }
        return page;
    }
}
