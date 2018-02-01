package by.andruhovich.subscription.command.user;

import by.andruhovich.subscription.command.BaseCommand;
import by.andruhovich.subscription.type.ClientType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LogoutCommand extends BaseCommand {
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        int clientId = -1;
        HttpSession session = request.getSession();

        request.setAttribute(CLIENT_ID, clientId);
        session.setAttribute(CLIENT_TYPE, ClientType.GUEST);
        return showPublicationList(request, response);
    }
}
