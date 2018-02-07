package by.andruhovich.subscription.service;

import by.andruhovich.subscription.dao.AccountManagerDAO;
import by.andruhovich.subscription.dao.UserManagerDAO;
import by.andruhovich.subscription.dao.impl.AccountDAO;
import by.andruhovich.subscription.dao.impl.UserDAO;
import by.andruhovich.subscription.entity.Account;
import by.andruhovich.subscription.exception.ConnectionTechnicalException;
import by.andruhovich.subscription.exception.DAOTechnicalException;
import by.andruhovich.subscription.exception.ServiceTechnicalException;
import by.andruhovich.subscription.pool.ConnectionFactory;

import java.math.BigDecimal;
import java.sql.Connection;

public class AccountService extends BaseService {
    private static final BigDecimal ZERO = new BigDecimal(0.00);
    private static final BigDecimal MAX_LOAN = new BigDecimal(50.00);

    public Account findAccountByUserId(String userId) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;

        int intUserId = Integer.parseInt(userId);

        try {
            connection = connectionFactory.getConnection();
            UserManagerDAO userManagerDAO = new UserDAO(connection);
            return userManagerDAO.findAccountByUserId(intUserId);
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e.getMessage());
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    public Account recharge(String userId, String sum) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;

        BigDecimal rechargeSum = new BigDecimal(sum);

        try {
            connection = connectionFactory.getConnection();
            AccountManagerDAO accountManagerDAO = new AccountDAO(connection);
            Account account = findAccountByUserId(userId);
            if (account.getLoan().compareTo(ZERO) == 0) {
                return accountManagerDAO.recharge(account.getAccountNumber(), rechargeSum, ZERO);
            }
            if (account.getLoan().compareTo(rechargeSum) > 0) {
                return accountManagerDAO.recharge(account.getAccountNumber(), ZERO, rechargeSum);
            }
            return accountManagerDAO.recharge(account.getAccountNumber(), rechargeSum.subtract(account.getLoan()), account.getLoan());
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e.getMessage());
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    public Account takeLoan(String userId, String sum) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;

        BigDecimal loanSum = new BigDecimal(sum);

        try {
            connection = connectionFactory.getConnection();
            AccountManagerDAO accountManagerDAO = new AccountDAO(connection);
            Account account = findAccountByUserId(userId);
            if (MAX_LOAN.compareTo(account.getLoan().add(loanSum)) < 0) {
                return null;
            }
            return accountManagerDAO.takeLoan(account.getAccountNumber(), loanSum);
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e.getMessage());
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }
}
