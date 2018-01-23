package by.andruhovich.subscription.filter;

import by.andruhovich.subscription.entity.Publication;
import by.andruhovich.subscription.exception.ServiceTechnicalException;
import by.andruhovich.subscription.manager.ConfigurationManager;
import by.andruhovich.subscription.manager.MessageManager;
import by.andruhovich.subscription.service.PublicationService;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebFilter( dispatcherTypes = {
        DispatcherType.REQUEST,
        DispatcherType.FORWARD,
        DispatcherType.INCLUDE},
        urlPatterns = { "/jsp/user/publication.jsp" })
public class PublicationFilter implements Filter {
    private PublicationService publicationService = new PublicationService();

    private static final String PAGE_NUMBER = "1";
    private static final String PUBLICATION_LIST_ATTRIBUTE = "publications";
    private static final String PUBLICATION_MESSAGE_ATTRIBUTE = "publicationIsAbsent";
    private static final String PUBLICATION_MESSAGE = "message.absent";

    public void init(FilterConfig fConfig) {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        MessageManager messageManager = MessageManager.getInstance();

        try {
            List<Publication> publications = publicationService.showPublications(PAGE_NUMBER);
            if (!publications.isEmpty()) {
                req.setAttribute(PUBLICATION_LIST_ATTRIBUTE, publications);
            } else {
                req.setAttribute(PUBLICATION_MESSAGE_ATTRIBUTE, "Publications" + messageManager.getProperty(PUBLICATION_MESSAGE));
            }

        } catch (ServiceTechnicalException e) {
            //log
        }
        chain.doFilter(req, resp);
    }

    public void destroy() {
    }
}
