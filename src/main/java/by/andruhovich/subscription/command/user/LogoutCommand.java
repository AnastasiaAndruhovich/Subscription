package by.andruhovich.subscription.command.user;

import by.andruhovich.subscription.command.BaseCommand;
import by.andruhovich.subscription.command.CommandResult;
import by.andruhovich.subscription.command.TransitionType;
import by.andruhovich.subscription.command.common.ShowEntityList;
import by.andruhovich.subscription.type.ClientType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LogoutCommand extends BaseCommand {
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        int clientId = -1;
        HttpSession session = request.getSession();
        session.removeAttribute(MESSAGE_ATTRIBUTE);

        request.setAttribute(CLIENT_ID, clientId);
        session.setAttribute(CLIENT_TYPE, ClientType.GUEST);
        return new CommandResult(TransitionType.FORWARD, ShowEntityList.showPublicationList(request, response));
    }
}
