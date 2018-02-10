package by.andruhovich.subscription.command.payment;

import by.andruhovich.subscription.command.BaseCommand;
import by.andruhovich.subscription.command.CommandResult;
import by.andruhovich.subscription.command.TransitionType;
import by.andruhovich.subscription.entity.Payment;
import by.andruhovich.subscription.exception.MissingResourceTechnicalException;
import by.andruhovich.subscription.exception.ServiceTechnicalException;
import by.andruhovich.subscription.manager.PageManager;
import by.andruhovich.subscription.service.PaymentService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Finds page according to relevant command find payment entity by user id
 */
public class FindPaymentByUserCommand extends BaseCommand {
    private PaymentService paymentService = new PaymentService();

    private static final String PAGE_NUMBER_ATTRIBUTE = "pageNumber";
    private static final String PAGE_COUNT_ATTRIBUTE = "pageCount";
    private static final String USER_ID_ATTRIBUTE = "userId";
    private static final String PAYMENT_LIST_ATTRIBUTE = "payments";

    private static final String PUBLICATION_USER_PAGE = "path.page.user.paymentByUser";

    private static final Logger LOGGER = LogManager.getLogger(FindPaymentByUserCommand.class);

    /**
     * @param request http request
     * @param response http response
     * @return command result including page and transition type
     */
    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        String page;
        PageManager pageManager = PageManager.getInstance();
        HttpSession session = request.getSession();
        session.removeAttribute(MESSAGE_ATTRIBUTE);

        String pageNumber = request.getParameter(PAGE_NUMBER_ATTRIBUTE);
        pageNumber = (pageNumber == null) ? "1" : pageNumber;
        String userId = request.getParameter(USER_ID_ATTRIBUTE);

        try {
            List<Payment> payments = paymentService.findPaymentByUserId(userId, pageNumber);
            if (!payments.isEmpty()) {
                int pageCount = paymentService.findPaymentPageCount();
                session.setAttribute(PAYMENT_LIST_ATTRIBUTE, payments);
                session.setAttribute(PAGE_NUMBER_ATTRIBUTE, pageNumber);
                session.setAttribute(PAGE_COUNT_ATTRIBUTE, pageCount);
            }
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
