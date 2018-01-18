package by.andruhovich.subscription.command;

import by.andruhovich.subscription.command.user.UserCommandMap;
import by.andruhovich.subscription.manager.MessageManager;

import javax.servlet.http.HttpServletRequest;

public class CommandFactory {
    public BaseCommand defineCommand(HttpServletRequest request) {
        BaseCommand command;
        final String WRONG_ACTION = "wrongAction";
        final String WRONG_ACTION_MESSAGE = "message.wrongaction";

        String action = request.getParameter("command");
        if (action == null || action.isEmpty()) {
            return null;
        }

        try {
            UserCommandMap userCommandMap = UserCommandMap.getInstance();
            command = userCommandMap.get(action.toUpperCase());
            return command;
        } catch (IllegalArgumentException e) {
            //TODO log
            MessageManager messageManager = MessageManager.getInstance();
            request.setAttribute(WRONG_ACTION, action + messageManager.getProperty(WRONG_ACTION_MESSAGE));
        }
        return null;
    }
}
