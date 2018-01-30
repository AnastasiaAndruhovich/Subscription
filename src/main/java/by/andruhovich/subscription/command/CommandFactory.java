package by.andruhovich.subscription.command;

import by.andruhovich.subscription.exception.UndefinedCommandTechnicalException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

public class CommandFactory {
    private static final Logger LOGGER = LogManager.getLogger(CommandFactory.class);

    public BaseCommand defineCommand(HttpServletRequest request) {
        BaseCommand command;
        final String COMMAND_ATTRIBUTE = "command";

        String action = request.getParameter(COMMAND_ATTRIBUTE);
        if (action == null || action.isEmpty()) {
            return null;
        }

        try {
            CommandMap commandMap = CommandMap.getInstance();
            command = commandMap.get(action.toUpperCase());
            return command;
        } catch (UndefinedCommandTechnicalException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
        return null;
    }
}
