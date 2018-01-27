package by.andruhovich.subscription.filter;

import by.andruhovich.subscription.command.publication.ShowPublicationCommand;
import by.andruhovich.subscription.type.ClientType;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(dispatcherTypes = {
        DispatcherType.REQUEST,
        DispatcherType.FORWARD,
        DispatcherType.INCLUDE},
        urlPatterns = { "/controller" },
        servletNames = { "ControllerServlet" })
public class ServletSecurityFilter implements Filter {
    public void init(FilterConfig fConfig) {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        final String CLIENT_TYPE = "clientType";
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession();
        ClientType type = (ClientType) session.getAttribute(CLIENT_TYPE);

        if (type == null) {
            type = ClientType.GUEST;
            session.setAttribute(CLIENT_TYPE, type);
            ShowPublicationCommand showPublicationCommand = new ShowPublicationCommand();
            String page = showPublicationCommand.execute(req, resp);
            RequestDispatcher dispatcher = request.getServletContext().getRequestDispatcher(page);
            dispatcher.forward(req, resp);
            return;
        }
        chain.doFilter(request, response);
    }

    public void destroy() {
    }
}