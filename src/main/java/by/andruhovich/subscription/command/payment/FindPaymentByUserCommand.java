package by.andruhovich.subscription.command.payment;

import by.andruhovich.subscription.command.BaseCommand;
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
import java.util.List;

public class FindPaymentByUserCommand extends BaseCommand {
    private PaymentService paymentService = new PaymentService();

    private static final String PAGE_NUMBER_ATTRIBUTE = "pageNumber";
    private static final String PAGE_COUNT_ATTRIBUTE = "pageCount";
    private static final String USER_ID = "userId";
    private static final String PAYMENT_LIST_ATTRIBUTE = "payments";

    private static final String PUBLICATION_USER_PAGE = "path.page.user.paymentList";

    private static final Logger LOGGER = LogManager.getLogger(FindPaymentByUserCommand.class);

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        String page;
        PageManager pageManager = PageManager.getInstance();

        String pageNumber = request.getParameter(PAGE_NUMBER_ATTRIBUTE);
        pageNumber = (pageNumber == null) ? "1" : pageNumber;
        String userId = request.getParameter(USER_ID);
        if (userId == null) {
            userId = ((Integer) request.getSession().getAttribute(CLIENT_ID)).toString();
        }

        try {
            List<Payment> payments = paymentService.findPaymentByUserId(userId, pageNumber);
            if (!payments.isEmpty()) {
                int pageCount = paymentService.findPaymentPageCount();
                request.setAttribute(PAYMENT_LIST_ATTRIBUTE, payments);
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
