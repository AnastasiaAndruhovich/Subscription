package by.andruhovich.subscription.command.subscription;

import by.andruhovich.subscription.command.BaseCommand;
import by.andruhovich.subscription.entity.Subscription;
import by.andruhovich.subscription.exception.MissingResourceTechnicalException;
import by.andruhovich.subscription.exception.ServiceTechnicalException;
import by.andruhovich.subscription.manager.PageManager;
import by.andruhovich.subscription.service.SubscriptionService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class FindSubscriptionByUserCommand extends BaseCommand {
    private SubscriptionService subscriptionService = new SubscriptionService();

    private static final String PAGE_NUMBER_ATTRIBUTE = "pageNumber";
    private static final String PAGE_COUNT_ATTRIBUTE = "pageCount";
    private static final String CLIENT_ID_ATTRIBUTE = "clientId";
    private static final String SUBSCRIPTION_LIST_ATTRIBUTE = "subscriptions";

    private static final String PUBLICATION_USER_PAGE = "path.page.user.subscriptionList";

    private static final Logger LOGGER = LogManager.getLogger(FindSubscriptionByUserCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String page;
        PageManager pageManager = PageManager.getInstance();

        String pageNumber = request.getParameter(PAGE_NUMBER_ATTRIBUTE);
        pageNumber = (pageNumber == null) ? "1" : pageNumber;
        Integer clientId = (Integer) request.getSession().getAttribute(CLIENT_ID_ATTRIBUTE);

        try {
            List<Subscription> subscriptions = subscriptionService.findSubscriptionByUserId(clientId.toString(), pageNumber);
            if (!subscriptions.isEmpty()) {
                int pageCount = subscriptionService.findSubscriptionPageCount();
                request.setAttribute(SUBSCRIPTION_LIST_ATTRIBUTE, subscriptions);
                request.setAttribute(PAGE_NUMBER_ATTRIBUTE, pageNumber);
                request.setAttribute(PAGE_COUNT_ATTRIBUTE, pageCount);
            }
            page = pageManager.getProperty(PUBLICATION_USER_PAGE);
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
