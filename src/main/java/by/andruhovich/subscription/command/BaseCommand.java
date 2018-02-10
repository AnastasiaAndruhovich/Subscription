package by.andruhovich.subscription.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Provides general method execute to define result page and transaction type
 */
public abstract class BaseCommand {
    protected static final String ERROR_PAGE = "/jsp/error/error.jsp";

    protected static final String CLIENT_TYPE = "clientType";
    protected static final String CLIENT_ID = "clientId";
    protected static final String LOCALE = "locale";

    protected static final String MESSAGE_ATTRIBUTE = "message";
    protected static final String ERROR_NAME_MESSAGE = "message.errorNameFormat";

    public abstract CommandResult execute(HttpServletRequest request, HttpServletResponse response);
}
