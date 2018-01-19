package by.andruhovich.subscription.servlet;

import by.andruhovich.subscription.command.BaseCommand;
import by.andruhovich.subscription.command.CommandFactory;
import by.andruhovich.subscription.manager.ConfigurationManager;
import by.andruhovich.subscription.manager.MessageManager;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/controller")
public class ControllerServlet extends HttpServlet {
    //Logger logger = LogManager.getLogger(ControllerServlet.class);

    @Override
    public void init() {
        //logger.log(Level.ERROR, "message -------------------");
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
        final String NULL_PAGE_MASSAGE = "message.nullpage";
        final String NULL_PAGE_NAME = "nullPage";

        CommandFactory commandFactory = new CommandFactory();
        BaseCommand command = commandFactory.defineCommand(request);
        if (command != null) {
            page = command.execute(request, response);
            if (page != null) {
                RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(page);
                dispatcher.forward(request, response);
            } else {
                ConfigurationManager configurationManager = ConfigurationManager.getInstance();
                MessageManager messageManager = MessageManager.getInstance();
                page = configurationManager.getProperty(ERROR_PAGE);
                request.getSession().setAttribute(NULL_PAGE_NAME, messageManager.getProperty(NULL_PAGE_MASSAGE));
                response.sendRedirect(request.getContextPath() + page);
            }
        } else {
            //logger.log(Level.ERROR, "message");
        }
    }

    @Override
    public void destroy() {
        //close connection pool
    }
}
