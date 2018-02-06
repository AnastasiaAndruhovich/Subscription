package by.andruhovich.subscription.command.subscription;

import by.andruhovich.subscription.command.BaseCommand;
import by.andruhovich.subscription.command.common.ShowEntityList;
import by.andruhovich.subscription.command.publication.AddPublicationCommand;
import by.andruhovich.subscription.entity.Subscription;
import by.andruhovich.subscription.exception.MissingResourceTechnicalException;
import by.andruhovich.subscription.exception.ServiceTechnicalException;
import by.andruhovich.subscription.manager.LocaleManager;
import by.andruhovich.subscription.manager.PageManager;
import by.andruhovich.subscription.service.PublicationService;
import by.andruhovich.subscription.service.SubscriptionService;
import by.andruhovich.subscription.service.UserService;
import by.andruhovich.subscription.type.ClientType;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

public class AddSubscriptionCommand extends BaseCommand {
    private UserService userService = new UserService();
    private SubscriptionService subscriptionService = new SubscriptionService();

    private static final String SUBSCRIBE_PAGE = "path.page.user.paySubscription";

    private static final String CLIENT_TYPE_ATTRIBUTE = "clientType";
    private static final String CLIENT_ID_ATTRIBUTE = "clientId";
    private static final String PUBLICATION_ID_ATTRIBUTE = "publicationId";
    private static final String SUBSCRIPTION_ATTRIBUTE = "subscription";
    private static final String ERROR_SUBSCRIBE_ATTRIBUTE = "errorSubscribe";

    private static final String ERROR_SUBSCRIBE_MESSAGE = "message.errorNoRights";
    private static final String ERROR_BLOCK_MESSAGE = "message.errorBlockedUser";

    private static final Logger LOGGER = LogManager.getLogger(AddPublicationCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String page;
        PageManager pageManager = PageManager.getInstance();
        Locale locale = (Locale)request.getSession().getAttribute(LOCALE);
        LocaleManager localeManager = new LocaleManager(locale);

        ClientType clientType = (ClientType) request.getSession().getAttribute(CLIENT_TYPE_ATTRIBUTE);
        Integer clientId = (Integer) request.getSession().getAttribute(CLIENT_ID_ATTRIBUTE);
        String publicationId = request.getParameter(PUBLICATION_ID_ATTRIBUTE);

        try {
            if (clientType.equals(ClientType.GUEST)) {
                String errorSubscribeMessage = localeManager.getProperty(ERROR_SUBSCRIBE_MESSAGE);
                request.setAttribute(ERROR_SUBSCRIBE_ATTRIBUTE, errorSubscribeMessage);
                return ShowEntityList.showPublicationList(request, response);
            }
            if (userService.isUserBlocked(clientId.toString())) {
                String errorSubscribeMessage = localeManager.getProperty(ERROR_BLOCK_MESSAGE);
                request.setAttribute(ERROR_SUBSCRIBE_ATTRIBUTE, errorSubscribeMessage);
                return ShowEntityList.showPublicationList(request, response);
            }

            Subscription subscription = subscriptionService.addSubscription(clientId.toString(), publicationId);
            request.setAttribute(SUBSCRIPTION_ATTRIBUTE, subscription);
            page = pageManager.getProperty(SUBSCRIBE_PAGE);
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
