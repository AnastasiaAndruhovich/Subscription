package by.andruhovich.subscription.command.account;

import by.andruhovich.subscription.command.BaseCommand;
import by.andruhovich.subscription.command.CommandResult;
import by.andruhovich.subscription.command.TransitionType;
import by.andruhovich.subscription.entity.Account;
import by.andruhovich.subscription.exception.MissingResourceTechnicalException;
import by.andruhovich.subscription.exception.ServiceTechnicalException;
import by.andruhovich.subscription.manager.LocaleManager;
import by.andruhovich.subscription.manager.PageManager;
import by.andruhovich.subscription.service.AccountService;
import by.andruhovich.subscription.validator.ServiceValidator;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Locale;

public class RechargeCommand extends BaseCommand {
    private AccountService accountService = new AccountService();

    private static final String RECHARGE_SUM_ATTRIBUTE = "rechargeSum";
    private static final String ACCOUNT_ATTRIBUTE = "account";

    private static final String INCORRECT_PRICE_MESSAGE = "message.incorrectMoneyFormat";

    private static final String ACCOUNT_BY_USER_PAGE = "path.page.user.accountByUser";

    private static final Logger LOGGER = LogManager.getLogger(FindAccountByUserCommand.class);

    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        String page;
        PageManager pageManager = PageManager.getInstance();
        Locale locale = (Locale)request.getSession().getAttribute(LOCALE);
        LocaleManager localeManager = new LocaleManager(locale);

        Integer userId = (Integer) request.getSession().getAttribute(CLIENT_ID);
        String rechargeSum = request.getParameter(RECHARGE_SUM_ATTRIBUTE);
        HttpSession session = request.getSession();
        session.removeAttribute(MESSAGE_ATTRIBUTE);

        try {
            if (!ServiceValidator.verifyPrice(rechargeSum)) {
                String incorrectPriceMessage = localeManager.getProperty(INCORRECT_PRICE_MESSAGE);
                session.setAttribute(MESSAGE_ATTRIBUTE, incorrectPriceMessage);
                Account account = accountService.findAccountByUserId(userId.toString());
                session.setAttribute(ACCOUNT_ATTRIBUTE, account);
                page = pageManager.getProperty(ACCOUNT_BY_USER_PAGE);
                return new CommandResult(TransitionType.FORWARD, page);
            }

            Account account = accountService.recharge(userId.toString(), rechargeSum);
            session.setAttribute(ACCOUNT_ATTRIBUTE, account);
            page = pageManager.getProperty(ACCOUNT_BY_USER_PAGE);
        } catch (ServiceTechnicalException e) {
            LOGGER.log(Level.ERROR, "Database error connection");
            page = ERROR_PAGE;
        } catch (MissingResourceTechnicalException e) {
            LOGGER.log(Level.ERROR, e.getMessage());
            page = ERROR_PAGE;
        }
        return new CommandResult(TransitionType.REDIRECT, page);
    }
}
