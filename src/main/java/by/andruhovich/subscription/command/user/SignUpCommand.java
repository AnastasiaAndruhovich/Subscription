package by.andruhovich.subscription.command.user;

import by.andruhovich.subscription.command.BaseCommand;
import by.andruhovich.subscription.command.CommandResult;
import by.andruhovich.subscription.command.TransitionType;
import by.andruhovich.subscription.command.common.ShowEntityList;
import by.andruhovich.subscription.converter.ClientDataConverter;
import by.andruhovich.subscription.exception.MissingResourceTechnicalException;
import by.andruhovich.subscription.exception.ServiceTechnicalException;
import by.andruhovich.subscription.manager.LocaleManager;
import by.andruhovich.subscription.manager.PageManager;
import by.andruhovich.subscription.service.UserService;
import by.andruhovich.subscription.type.ClientType;
import by.andruhovich.subscription.validator.ServiceValidator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.Locale;

/**
 * Finds page according to relevant command sign up
 */
public class SignUpCommand extends BaseCommand {
    private UserService userService = new UserService();

    private static final String LOGIN_ATTRIBUTE = "login";
    private static final String PASSWORD_ATTRIBUTE = "password";
    private static final String CONFIRM_PASSWORD_ATTRIBUTE = "confirmPassword";
    private static final String LAST_NAME_ATTRIBUTE = "lastName";
    private static final String FIRST_NAME_ATTRIBUTE = "firstName";
    private static final String BIRTH_DATE_ATTRIBUTE = "birthDate";
    private static final String ADDRESS_ATTRIBUTE = "address";
    private static final String CITY_ATTRIBUTE = "city";
    private static final String POSTAL_INDEX_ATTRIBUTE = "postalIndex";

    private static final String ERROR_SIGN_UP_MESSAGE = "message.errorSignUp";
    private static final String ERROR_SIGN_UP_LOGIN_MESSAGE = "message.errorSignUpLogin";
    private static final String ERROR_SIGN_UP_PASSWORD_MESSAGE = "message.errorSignUpPassword";
    private static final String ERROR_SIGN_UP_CONFIRM_PASSWORD_MESSAGE = "message.errorSignUpConfirmPassword";
    private static final String ERROR_SIGN_UP_BIRTH_DATE_MESSAGE = "message.errorSignUpBirthDate";
    private static final String ERROR_SIGN_UP_POSTAL_INDEX_MESSAGE = "message.errorSignUpPostalIndex";

    private static final String SIGN_UP_PAGE = "path.page.user.signUp";

    private static final Logger LOGGER = LogManager.getLogger(SignUpCommand.class);

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

        String login = request.getParameter(LOGIN_ATTRIBUTE);
        String password = request.getParameter(PASSWORD_ATTRIBUTE);
        String confirmPassword = request.getParameter(CONFIRM_PASSWORD_ATTRIBUTE);
        String lastName = request.getParameter(LAST_NAME_ATTRIBUTE);
        String firstName = request.getParameter(FIRST_NAME_ATTRIBUTE);
        String birthDate = request.getParameter(BIRTH_DATE_ATTRIBUTE);
        String address = request.getParameter(ADDRESS_ATTRIBUTE);
        String city = request.getParameter(CITY_ATTRIBUTE);
        String postalIndex = request.getParameter(POSTAL_INDEX_ATTRIBUTE);

        try {
            if (!ServiceValidator.verifyName(lastName) || !ServiceValidator.verifyName(firstName) ||
                    !ServiceValidator.verifyName(city)) {
                page = pageManager.getProperty(SIGN_UP_PAGE);
                String errorNameMessage = localeManager.getProperty(ERROR_NAME_MESSAGE);
                session.setAttribute(MESSAGE_ATTRIBUTE, errorNameMessage);
                return new CommandResult(TransitionType.REDIRECT, page);
            }

            if (!ServiceValidator.verifyLogin(login)) {
                page = pageManager.getProperty(SIGN_UP_PAGE);
                String errorSignUpMessage = localeManager.getProperty(ERROR_SIGN_UP_LOGIN_MESSAGE);
                session.setAttribute(MESSAGE_ATTRIBUTE, errorSignUpMessage);
                return new CommandResult(TransitionType.REDIRECT, page);
            }
            if (!ServiceValidator.verifyPassword(password)) {
                page = pageManager.getProperty(SIGN_UP_PAGE);
                String errorSignUpMessage = localeManager.getProperty(ERROR_SIGN_UP_PASSWORD_MESSAGE);
                session.setAttribute(MESSAGE_ATTRIBUTE, errorSignUpMessage);
                return new CommandResult(TransitionType.REDIRECT, page);
            }
            if (!ServiceValidator.confirmPassword(password, confirmPassword)) {
                page = pageManager.getProperty(SIGN_UP_PAGE);
                String errorSignUpMessage = localeManager.getProperty(ERROR_SIGN_UP_CONFIRM_PASSWORD_MESSAGE);
                session.setAttribute(MESSAGE_ATTRIBUTE, errorSignUpMessage);
                return new CommandResult(TransitionType.REDIRECT, page);
            }
            if (!ServiceValidator.verifyDate(birthDate)) {
                page = pageManager.getProperty(SIGN_UP_PAGE);
                String errorSignUpMessage = localeManager.getProperty(ERROR_SIGN_UP_BIRTH_DATE_MESSAGE);
                session.setAttribute(MESSAGE_ATTRIBUTE, errorSignUpMessage);
                return new CommandResult(TransitionType.REDIRECT, page);
            }
            if (!ServiceValidator.verifyPostalIndex(postalIndex)) {
                page = pageManager.getProperty(SIGN_UP_PAGE);
                String errorSignUpMessage = localeManager.getProperty(ERROR_SIGN_UP_POSTAL_INDEX_MESSAGE);
                session.setAttribute(MESSAGE_ATTRIBUTE, errorSignUpMessage);
                return new CommandResult(TransitionType.REDIRECT, page);
            }

            Date date = ClientDataConverter.convertStringToDate(birthDate);

            int result = userService.signUp(lastName, firstName, date, address, city, postalIndex, login, password);
            if (result != -1) {
                session.setAttribute(CLIENT_TYPE, ClientType.USER);
                session.setAttribute(CLIENT_ID, result);
                page = ShowEntityList.showPublicationList(request, response);
            } else {
                page = pageManager.getProperty(SIGN_UP_PAGE);
                String errorSignUpMessage = localeManager.getProperty(ERROR_SIGN_UP_MESSAGE);
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
