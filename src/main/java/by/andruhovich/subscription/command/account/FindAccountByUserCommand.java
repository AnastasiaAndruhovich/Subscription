package by.andruhovich.subscription.command.account;

import by.andruhovich.subscription.command.BaseCommand;
import by.andruhovich.subscription.command.CommandResult;
import by.andruhovich.subscription.command.TransitionType;
import by.andruhovich.subscription.entity.Account;
import by.andruhovich.subscription.exception.MissingResourceTechnicalException;
import by.andruhovich.subscription.exception.ServiceTechnicalException;
import by.andruhovich.subscription.manager.PageManager;
import by.andruhovich.subscription.service.AccountService;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FindAccountByUserCommand extends BaseCommand {
    private AccountService accountService = new AccountService();

    private static final String ACCOUNT_ATTRIBUTE = "account";

    private static final String ACCOUNT_BY_USER_PAGE = "path.page.user.accountByUser";

    private static final Logger LOGGER = LogManager.getLogger(FindAccountByUserCommand.class);

    @Override
    public CommandResult execute(HttpServletRequest request, HttpServletResponse response) {
        String page;
        PageManager pageManager = PageManager.getInstance();
        Integer userId = (Integer) request.getSession().getAttribute(CLIENT_ID);

        try {
            Account account = accountService.findAccountByUserId(userId.toString());
            request.getSession().setAttribute(ACCOUNT_ATTRIBUTE, account);
            page = pageManager.getProperty(ACCOUNT_BY_USER_PAGE);
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
