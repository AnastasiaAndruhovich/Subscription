package by.andruhovich.subscription.command.subscription;

import by.andruhovich.subscription.command.BaseCommand;
import by.andruhovich.subscription.command.CommandResult;
import by.andruhovich.subscription.command.TransitionType;
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
import javax.servlet.http.HttpSession;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FindSubscriptionByUserCommand extends BaseCommand {
    private static final String PAGE_NUMBER = "pageNumber";
    private static final String PAGE_COUNT = "pageCount";

    private static final Logger LOGGER = LogManager.getLogger(FindSubscriptionByUserCommand.class);

    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        SubscriptionService subscriptionService = new SubscriptionService();

        final String USER_ID_ATTRIBUTE = "userId";
        final String SUBSCRIPTION_LIST_ATTRIBUTE = "subscriptions";
        final String CURRENT_DATE_ATTRIBUTE = "currentDate";

        final String PUBLICATION_USER_PAGE = "path.page.user.subscriptionByUser";

        String page;
        PageManager pageManager = PageManager.getInstance();
        HttpSession session = request.getSession();

        String pageNumber = request.getParameter(PAGE_NUMBER);
        pageNumber = (pageNumber == null) ? "1" : pageNumber;
        String userId = request.getParameter(USER_ID_ATTRIBUTE);
        session.setAttribute(USER_ID_ATTRIBUTE, userId);

        Date date = Calendar.getInstance().getTime();
        session.setAttribute(CURRENT_DATE_ATTRIBUTE, date);

        try {
            List<Subscription> subscriptions = subscriptionService.findSubscriptionByUserId(userId, pageNumber);
            int pageCount = subscriptionService.findSubscriptionPageCount();
            session.setAttribute(SUBSCRIPTION_LIST_ATTRIBUTE, subscriptions);
            session.setAttribute(PAGE_NUMBER, pageNumber);
            session.setAttribute(PAGE_COUNT, pageCount);
            page = pageManager.getProperty(PUBLICATION_USER_PAGE);
        } catch (ServiceTechnicalException e) {
            LOGGER.log(Level.ERROR, "Database error connection");
            page = ERROR_PAGE;
        } catch (MissingResourceTechnicalException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
            page = ERROR_PAGE;
        }
        return new CommandResult(TransitionType.FORWARD, page);
    }
}
