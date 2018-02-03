package by.andruhovich.subscription.command;

import by.andruhovich.subscription.entity.Publication;
import by.andruhovich.subscription.exception.MissingResourceTechnicalException;
import by.andruhovich.subscription.exception.ServiceTechnicalException;
import by.andruhovich.subscription.manager.LocaleManager;
import by.andruhovich.subscription.manager.PageManager;
import by.andruhovich.subscription.service.PublicationService;
import by.andruhovich.subscription.type.ClientType;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Locale;

public abstract class BaseCommand {
    protected static final String ERROR_PAGE = "/jsp/error/error.jsp";

    protected static final String INFORMATION_IS_ABSENT_ATTRIBUTE = "informationIsAbsent";
    protected static final String INFORMATION_IS_ABSENT_MESSAGE = "message.informationIsAbsent";

    protected static final String CLIENT_TYPE = "clientType";
    protected static final String CLIENT_ID = "clientId";
    protected static final String LOCALE = "locale";

    public abstract String execute(HttpServletRequest request, HttpServletResponse response);
}
