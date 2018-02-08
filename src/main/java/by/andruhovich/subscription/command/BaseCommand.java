package by.andruhovich.subscription.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class BaseCommand {
    protected static final String ERROR_PAGE = "/jsp/error/error.jsp";

    protected static final String CLIENT_TYPE = "clientType";
    protected static final String CLIENT_ID = "clientId";
    protected static final String LOCALE = "locale";

    protected static final String MESSAGE_ATTRIBUTE = "message";

    public abstract String execute(HttpServletRequest request, HttpServletResponse response);
}
