package by.andruhovich.subscription.command.user;

import by.andruhovich.subscription.command.BaseCommand;
import by.andruhovich.subscription.command.common.ShowEntityList;
import by.andruhovich.subscription.exception.MissingResourceTechnicalException;
import by.andruhovich.subscription.exception.ServiceTechnicalException;
import by.andruhovich.subscription.manager.LocaleManager;
import by.andruhovich.subscription.service.UserService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

public class BlockUserCommand extends BaseCommand {
    private UserService userService = new UserService();

    private static final String CLIENT_ID_ATTRIBUTE = "clientId";
    private static final String USER_ID_ATTRIBUTE = "userId";

    private static final String ERROR_BLOCK_MESSAGE = "message.errorBlockUser";

    private static final Logger LOGGER = LogManager.getLogger(BlockUserCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String page;
        Locale locale = (Locale) request.getSession().getAttribute(LOCALE);
        LocaleManager localeManager = new LocaleManager(locale);

        String userId = request.getParameter(USER_ID_ATTRIBUTE);
        Integer adminId = (Integer) request.getSession().getAttribute(CLIENT_ID_ATTRIBUTE);

        try {
            if (!userService.blockUser(userId, adminId.toString())) {
                String errorBlockUserMessage = localeManager.getProperty(ERROR_BLOCK_MESSAGE);
                request.setAttribute(MESSAGE_ATTRIBUTE, errorBlockUserMessage);
            }
            return ShowEntityList.showUserList(request, response);
        } catch (ServiceTechnicalException e) {
            LOGGER.log(Level.ERROR, "Database error connection");
            page = ERROR_PAGE;
        } catch (MissingResourceTechnicalException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
            page = ERROR_PAGE;
        }
        return page;
    }
}
