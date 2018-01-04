package by.andruhovich.subscription.command;

import by.andruhovich.subscription.command.BaseCommand;
import by.andruhovich.subscription.command.CommandMap;
import by.andruhovich.subscription.manager.MessageManager;

import javax.servlet.http.HttpServletRequest;

public class CommandFactory {
    public BaseCommand defineCommand(HttpServletRequest request) {
        BaseCommand command;
        // извлечение имени команды из запроса
        String action = request.getParameter("command");
        if (action == null || action.isEmpty()) {
            // если команда не задана в текущем запросе emptyCommand???
            return null;
        }
        // получение объекта, соответствующего команде
        try {
            CommandMap commandMap = CommandMap.getInstance();
            command = commandMap.get(action.toUpperCase());
            return command;
        } catch (IllegalArgumentException e) {
            request.setAttribute("wrongAction", action + MessageManager.getProperty("message.wrongaction"));
        }
        return null;
    }
}
