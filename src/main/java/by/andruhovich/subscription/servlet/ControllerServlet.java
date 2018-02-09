package by.andruhovich.subscription.servlet;

import by.andruhovich.subscription.command.BaseCommand;
import by.andruhovich.subscription.command.CommandFactory;
import by.andruhovich.subscription.command.CommandResult;
import by.andruhovich.subscription.command.TransitionType;
import by.andruhovich.subscription.exception.MissingResourceTechnicalException;
import by.andruhovich.subscription.manager.LocaleManager;
import by.andruhovich.subscription.pool.ConnectionPool;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

@WebServlet("/controller")
public class ControllerServlet extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(ControllerServlet.class);

    @Override
    public void init() {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CommandResult commandResult;
        String page;
        final String ERROR_PAGE = "/jsp/error/error.jsp";

        final String UNDEFINED_COMMAND_ATTRIBUTE = "undefinedCommand";
        final String NULL_PAGE_ATTRIBUTE = "nullPage";

        final String UNDEFINED_COMMAND_MESSAGE = "message.undefinedCommand";
        final String NULL_PAGE_MESSAGE = "message.nullPage";

        final String LOCALE = "locale";

        CommandFactory commandFactory = new CommandFactory();
        BaseCommand command = commandFactory.defineCommand(request);
        Locale locale = (Locale)request.getSession().getAttribute(LOCALE);
        LocaleManager localeManager = new LocaleManager(locale);

        try {
            if (command != null) {
                commandResult = command.execute(request, response);
                page = commandResult.getPage();
                if (page != null) {
                    if (TransitionType.REDIRECT.equals(commandResult.getTransitionType())) {
                        response.sendRedirect(page);
                    } else {
                        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(page);
                        dispatcher.forward(request, response);
                    }
                } else {
                    page = ERROR_PAGE;
                    request.getSession().setAttribute(NULL_PAGE_ATTRIBUTE, localeManager.getProperty(NULL_PAGE_MESSAGE));
                    response.sendRedirect(request.getContextPath() + page);
                }
            } else {
                page = ERROR_PAGE;
                request.getSession().setAttribute(UNDEFINED_COMMAND_ATTRIBUTE, localeManager.getProperty(UNDEFINED_COMMAND_MESSAGE));
                response.sendRedirect(request.getContextPath() + page);
            }
        } catch (MissingResourceTechnicalException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
        }
    }

    @Override
    public void destroy() {
        LOGGER.log(Level.INFO, "Request for destroy connection pool");
        ConnectionPool connectionPool = ConnectionPool.getInstance();
        connectionPool.closeConnectionPool();
        LOGGER.log(Level.INFO, "Request for destroy connection pool - succeed");
    }
}
