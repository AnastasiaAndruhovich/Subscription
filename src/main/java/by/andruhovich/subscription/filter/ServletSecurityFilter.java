package by.andruhovich.subscription.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = { "/controller" }, servletNames = { "ControllerServlet" })
public class ServletSecurityFilter implements Filter {
    public void init(FilterConfig fConfig) {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession();
        ClientType type = (ClientType) session.getAttribute("userType");
        if (type == null) {
            type = ClientType.GUEST;
            session.setAttribute("userType", type);
            RequestDispatcher dispatcher = request.getServletContext()
                    .getRequestDispatcher("/jsp/guest/publication.jsp");
            dispatcher.forward(req, resp);
            return;
        }
        chain.doFilter(request, response);
    }

    public void destroy() {
    }
}