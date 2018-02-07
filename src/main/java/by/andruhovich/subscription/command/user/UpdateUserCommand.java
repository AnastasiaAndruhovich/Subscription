package by.andruhovich.subscription.command.user;

import by.andruhovich.subscription.command.BaseCommand;
import by.andruhovich.subscription.command.common.ShowEntityList;
import by.andruhovich.subscription.converter.ClientDataConverter;
import by.andruhovich.subscription.entity.User;
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
import java.util.Date;
import java.util.Locale;

public class UpdateUserCommand extends BaseCommand {
    private UserService userService = new UserService();

    private static final String USER_ATTRIBUTE = "user";
    private static final String LOGIN_ATTRIBUTE = "login";
    private static final String LAST_NAME_ATTRIBUTE = "lastName";
    private static final String FIRST_NAME_ATTRIBUTE = "firstName";
    private static final String BIRTH_DATE_ATTRIBUTE = "birthDate";
    private static final String ADDRESS_ATTRIBUTE = "address";
    private static final String CITY_ATTRIBUTE = "city";
    private static final String POSTAL_INDEX_ATTRIBUTE = "postalIndex";
    private static final String ERROR_UPDATE_USER_ATTRIBUTE = "result";

    private static final String ERROR_UPDATE_USER_MESSAGE = "message.errorUpdateUser";
    private static final String ERROR_LOGIN_MESSAGE = "message.errorLogin";
    private static final String ERROR_BIRTH_DATE_MESSAGE = "message.errorBirthDate";
    private static final String ERROR_POSTAL_INDEX_MESSAGE = "message.errorPostalIndex";

    private static final String EDIT_PROFILE_PAGE = "path.page.user.editProfile";

    private static final Logger LOGGER = LogManager.getLogger(SignUpCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String page;
        PageManager pageManager = PageManager.getInstance();
        Locale locale = (Locale)request.getSession().getAttribute(LOCALE);
        LocaleManager localeManager = new LocaleManager(locale);

        String login = request.getParameter(LOGIN_ATTRIBUTE);
        String lastName = request.getParameter(LAST_NAME_ATTRIBUTE);
        String firstName = request.getParameter(FIRST_NAME_ATTRIBUTE);
        String birthDate = request.getParameter(BIRTH_DATE_ATTRIBUTE);
        String address = request.getParameter(ADDRESS_ATTRIBUTE);
        String city = request.getParameter(CITY_ATTRIBUTE);
        String postalIndex = request.getParameter(POSTAL_INDEX_ATTRIBUTE);

        Integer id = (Integer) request.getSession().getAttribute(CLIENT_ID);

        try {
            User user = userService.findUserById(id.toString());
            request.setAttribute(USER_ATTRIBUTE, user);

            if (!ServiceValidator.verifyLogin(login)) {
                page = pageManager.getProperty(EDIT_PROFILE_PAGE);
                String errorSignUpMessage = localeManager.getProperty(ERROR_LOGIN_MESSAGE);
                request.setAttribute(ERROR_UPDATE_USER_ATTRIBUTE, errorSignUpMessage);
                return page;
            }

            if (!ServiceValidator.verifyDate(birthDate)) {
                page = pageManager.getProperty(EDIT_PROFILE_PAGE);
                String errorSignUpMessage = localeManager.getProperty(ERROR_BIRTH_DATE_MESSAGE);
                request.setAttribute(ERROR_UPDATE_USER_ATTRIBUTE, errorSignUpMessage);
                return page;
            }
            if (!ServiceValidator.verifyPostalIndex(postalIndex)) {
                page = pageManager.getProperty(EDIT_PROFILE_PAGE);
                String errorSignUpMessage = localeManager.getProperty(ERROR_POSTAL_INDEX_MESSAGE);
                request.setAttribute(ERROR_UPDATE_USER_ATTRIBUTE, errorSignUpMessage);
                return page;
            }

            Date date = ClientDataConverter.convertStringToDate(birthDate);

            if (!userService.updateUser(id.toString(), lastName, firstName, date, address, city, postalIndex, login)) {
                String errorSignUpMessage = localeManager.getProperty(ERROR_UPDATE_USER_MESSAGE);
                request.setAttribute(ERROR_UPDATE_USER_ATTRIBUTE, errorSignUpMessage);
            }
            page = ShowEntityList.showUser(request, response);
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
