package by.andruhovich.subscription.command;

import by.andruhovich.subscription.manager.ConfigurationManager;
import by.andruhovich.subscription.manager.MessageManager;
import by.andruhovich.subscription.receiver.UserReceiver;

import javax.servlet.http.HttpServletRequest;

public class LoginCommand implements BaseCommand{
    private UserReceiver userReceiver = new UserReceiver();
    private static final String LOGIN = "login";
    private static final String PASSWORD = "password";

    public String execute(HttpServletRequest request) {
        String page;
        //бизнес-логика? сделать методы static?
        String login = request.getParameter(LOGIN);
        String password = request.getParameter(PASSWORD);

        if (userReceiver.checkLogin(login, password)) {
            request.setAttribute("user", login);
            page = ConfigurationManager.getProperty("path.page.main");
        } else {
            request.setAttribute("errorLoginPassMessage", MessageManager.getProperty("message.error"));
            page = ConfigurationManager.getProperty("path.page.login");
        }
        return page;
    }
}
