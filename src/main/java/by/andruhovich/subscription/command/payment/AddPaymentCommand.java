package by.andruhovich.subscription.command.payment;

import by.andruhovich.subscription.command.BaseCommand;
import by.andruhovich.subscription.entity.Payment;
import by.andruhovich.subscription.entity.Subscription;
import by.andruhovich.subscription.exception.MissingResourceTechnicalException;
import by.andruhovich.subscription.exception.ServiceTechnicalException;
import by.andruhovich.subscription.manager.LocaleManager;
import by.andruhovich.subscription.manager.PageManager;
import by.andruhovich.subscription.service.PaymentService;
import by.andruhovich.subscription.service.SubscriptionService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

public class AddPaymentCommand extends BaseCommand {
    private PaymentService paymentService = new PaymentService();
    private SubscriptionService subscriptionService = new SubscriptionService();

    private static final String SUBSCRIBE_PAGE = "path.page.user.paySubscription";

    private static final String SUBSCRIPTION_ID_ATTRIBUTE = "subscriptionId";
    private static final String SUBSCRIPTION_ATTRIBUTE = "subscription";

    private static final String SUCCESSFUL_PAY_SUBSCRIPTION_MESSAGE = "message.successfulPaySubscription";
    private static final String ERROR_PAY_SUBSCRIPTION_MESSAGE = "message.errorPaySubscription";

    private static final Logger LOGGER = LogManager.getLogger(AddPaymentCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String page;
        PageManager pageManager = PageManager.getInstance();
        Locale locale = (Locale) request.getSession().getAttribute(LOCALE);
        LocaleManager localeManager = new LocaleManager(locale);

        String subscriptionId = request.getParameter(SUBSCRIPTION_ID_ATTRIBUTE);
        Integer clientId = (Integer) request.getSession().getAttribute(CLIENT_ID);

        try {
            Payment payment = paymentService.addPayment(clientId.toString(), subscriptionId);
            if (payment.isStatement()) {
                String successfulPaySubscriptionMessage = localeManager.getProperty(SUCCESSFUL_PAY_SUBSCRIPTION_MESSAGE);
                request.setAttribute(MESSAGE_ATTRIBUTE, successfulPaySubscriptionMessage);
            }
            else {
                String errorPaySubscriptionMessage = localeManager.getProperty(ERROR_PAY_SUBSCRIPTION_MESSAGE);
                request.setAttribute(MESSAGE_ATTRIBUTE, errorPaySubscriptionMessage);
            }
            Subscription subscription = subscriptionService.findSubscriptionById(subscriptionId);
            request.setAttribute(SUBSCRIPTION_ATTRIBUTE, subscription);
            return pageManager.getProperty(SUBSCRIBE_PAGE);
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
