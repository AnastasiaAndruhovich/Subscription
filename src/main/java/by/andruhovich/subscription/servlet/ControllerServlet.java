package by.andruhovich.subscription.servlet;

import by.andruhovich.subscription.command.BaseCommand;
import by.andruhovich.subscription.command.CommandFactory;
import by.andruhovich.subscription.manager.ConfigurationManager;
import by.andruhovich.subscription.manager.MessageManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
        String page;
        final String ERROR_PAGE = "path.page.error";
        final String NULL_PAGE_MESSAGE = "message.nullpage";
        final String NULL_PAGE_NAME = "nullPage";
        final String UNDEFINED_COMMAND_MESSAGE = "message.undefinedcommand";
        final String UNDEFINED_COMMAND_PAGE_NAME = "undefinedCommand";

        CommandFactory commandFactory = new CommandFactory();
        BaseCommand command = commandFactory.defineCommand(request);
        ConfigurationManager configurationManager = ConfigurationManager.getInstance();
        MessageManager messageManager = MessageManager.getInstance();

        if (command != null) {
            page = command.execute(request, response);
            if (page != null) {
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(page);
                dispatcher.forward(request, response);
            } else {
                page = configurationManager.getProperty(ERROR_PAGE);
                request.getSession().setAttribute(NULL_PAGE_NAME, messageManager.getProperty(NULL_PAGE_MESSAGE));
                response.sendRedirect(request.getContextPath() + page);
            }
        } else {
            page = configurationManager.getProperty(ERROR_PAGE);
            request.getSession().setAttribute(UNDEFINED_COMMAND_PAGE_NAME, messageManager.getProperty(UNDEFINED_COMMAND_MESSAGE));
            response.sendRedirect(request.getContextPath() + page);
        }
    }

    @Override
    public void destroy() {
        //close connection pool
    }
}
