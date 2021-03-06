package by.andruhovich.subscription.command.user;

import by.andruhovich.subscription.command.BaseCommand;
import by.andruhovich.subscription.command.CommandResult;
import by.andruhovich.subscription.command.TransitionType;
import by.andruhovich.subscription.exception.MissingResourceTechnicalException;
import by.andruhovich.subscription.exception.ServiceTechnicalException;
import by.andruhovich.subscription.manager.LocaleManager;
import by.andruhovich.subscription.manager.PageManager;
import by.andruhovich.subscription.service.UserService;
import by.andruhovich.subscription.validator.ServiceValidator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Locale;

/**
 * Finds page according to relevant command change password
 */
public class ChangePasswordCommand extends BaseCommand {
    private UserService userService = new UserService();

    private static final String OLD_PASSWORD_ATTRIBUTE = "oldPassword";
    private static final String NEW_PASSWORD_ATTRIBUTE = "newPassword";
    private static final String REPEAT_PASSWORD_ATTRIBUTE = "repeatPassword";

    private static final String ERROR_OLD_PASSWORD_MESSAGE = "message.errorOldPassword";
    private static final String ERROR_NEW_PASSWORD_MESSAGE = "message.errorPassword";
    private static final String ERROR_CONFIRM_PASSWORD_MESSAGE = "message.errorConfirmPassword";
    private static final String ERROR_CHANGE_PASSWORD_MESSAGE = "message.errorChangePassword";
    private static final String SUCCESSFUL_CHANGE_PASSWORD_MESSAGE = "message.successfulChangePassword";

    private static final String CHANGE_PASSWORD_PAGE = "path.page.user.changePassword";

    private static final Logger LOGGER = LogManager.getLogger(ChangePasswordCommand.class);

    /**
     * @param request http request
     * @param response http response
     * @return command result including page and transition type
     */
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        String page;
        PageManager pageManager = PageManager.getInstance();
        Locale locale = (Locale)request.getSession().getAttribute(LOCALE);
        LocaleManager localeManager = new LocaleManager(locale);
        HttpSession session = request.getSession();
        session.removeAttribute(MESSAGE_ATTRIBUTE);

        String oldPassword = request.getParameter(OLD_PASSWORD_ATTRIBUTE);
        String newPassword = request.getParameter(NEW_PASSWORD_ATTRIBUTE);
        String repeatPassword = request.getParameter(REPEAT_PASSWORD_ATTRIBUTE);

        Integer id = (Integer) session.getAttribute(CLIENT_ID);

        try {
            page = pageManager.getProperty(CHANGE_PASSWORD_PAGE);
            if (!userService.confirmPassword(id.toString(), oldPassword)) {
                String errorChangePasswordMessage = localeManager.getProperty(ERROR_OLD_PASSWORD_MESSAGE);
                session.setAttribute(MESSAGE_ATTRIBUTE, errorChangePasswordMessage);
                return new CommandResult(TransitionType.REDIRECT, page);
            }

            if (!ServiceValidator.verifyPassword(newPassword)) {
                String errorChangePasswordMessage = localeManager.getProperty(ERROR_NEW_PASSWORD_MESSAGE);
                session.setAttribute(MESSAGE_ATTRIBUTE, errorChangePasswordMessage);
                return new CommandResult(TransitionType.REDIRECT, page);
            }
            if (!ServiceValidator.confirmPassword(newPassword, repeatPassword)) {
                String errorSignUpMessage = localeManager.getProperty(ERROR_CONFIRM_PASSWORD_MESSAGE);
                session.setAttribute(MESSAGE_ATTRIBUTE, errorSignUpMessage);
                return new CommandResult(TransitionType.REDIRECT, page);
            }

            if (!userService.changePassword(id.toString(), newPassword)) {
                String errorSignUpMessage = localeManager.getProperty(ERROR_CHANGE_PASSWORD_MESSAGE);
                session.setAttribute(MESSAGE_ATTRIBUTE, errorSignUpMessage);
            }
            else {
                String errorSignUpMessage = localeManager.getProperty(SUCCESSFUL_CHANGE_PASSWORD_MESSAGE);
                session.setAttribute(MESSAGE_ATTRIBUTE, errorSignUpMessage);
            }
        } catch (ServiceTechnicalException e) {
            LOGGER.log(Level.ERROR, "Database error connection");
            page = ERROR_PAGE;
        } catch (MissingResourceTechnicalException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
            page = ERROR_PAGE;
        }
        return new CommandResult(TransitionType.REDIRECT, page);
    }
}
