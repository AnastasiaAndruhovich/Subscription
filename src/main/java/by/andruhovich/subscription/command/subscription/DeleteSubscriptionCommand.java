package by.andruhovich.subscription.command.subscription;

import by.andruhovich.subscription.command.BaseCommand;
import by.andruhovich.subscription.command.common.ShowEntityList;
import by.andruhovich.subscription.exception.MissingResourceTechnicalException;
import by.andruhovich.subscription.exception.ServiceTechnicalException;
import by.andruhovich.subscription.manager.LocaleManager;
import by.andruhovich.subscription.service.SubscriptionService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

public class DeleteSubscriptionCommand extends BaseCommand {
    private SubscriptionService subscriptionService = new SubscriptionService();

    private static final String SUBSCRIPTION_ID_ATTRIBUTE = "subscriptionId";
    private static final String RESULT_DELETE_SUBSCRIPTION_ATTRIBUTE = "result";

    private static final String ERROR_DELETE_SUBSCRIPTION_MESSAGE = "message.errorDeleteSubscription";

    private static final Logger LOGGER = LogManager.getLogger(DeleteSubscriptionCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        Locale locale = (Locale) request.getSession().getAttribute(LOCALE);
        LocaleManager localeManager = new LocaleManager(locale);

        String subscriptionId = request.getParameter(SUBSCRIPTION_ID_ATTRIBUTE);

        try {
            if (!subscriptionService.deleteSubscription(subscriptionId)) {
                String errorDeleteSubscriptionMessage = localeManager.getProperty(ERROR_DELETE_SUBSCRIPTION_MESSAGE);
                request.setAttribute(RESULT_DELETE_SUBSCRIPTION_ATTRIBUTE, errorDeleteSubscriptionMessage);
            }
            return ShowEntityList.findSubscriptionByUser(request, response);
        } catch (ServiceTechnicalException e) {
            LOGGER.log(Level.ERROR, "Database error connection");
            return ERROR_PAGE;
        } catch (MissingResourceTechnicalException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
            return ERROR_PAGE;
        }
    }
}
