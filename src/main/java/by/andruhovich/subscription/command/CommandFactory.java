package by.andruhovich.subscription.command;

import by.andruhovich.subscription.exception.MissingResourceTechnicalException;
import by.andruhovich.subscription.manager.MessageManager;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

public class CommandFactory {
    private static final Logger LOGGER = LogManager.getLogger(CommandFactory.class);

    public BaseCommand defineCommand(HttpServletRequest request) {
        BaseCommand command;
        final String WRONG_ACTION = "wrongAction";
        final String WRONG_ACTION_MESSAGE = "message.wrongaction";

        String action = request.getParameter("command");
        if (action == null || action.isEmpty()) {
            return null;
        }

        try {
            CommandMap commandMap = CommandMap.getInstance();
            command = commandMap.get(action.toUpperCase());
            return command;
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.ERROR, "Command is undefined");
            MessageManager messageManager = MessageManager.getInstance();
            try {
                request.setAttribute(WRONG_ACTION, action + messageManager.getProperty(WRONG_ACTION_MESSAGE));
            } catch (MissingResourceTechnicalException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }
}
