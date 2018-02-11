package by.andruhovich.subscription.dao;

import by.andruhovich.subscription.entity.Account;
import by.andruhovich.subscription.exception.DAOTechnicalException;

import java.math.BigDecimal;
import java.sql.Connection;

/**
 * Concrete Manager extends ManagerDAO parametrize by Account entity
 */
public abstract class AccountManagerDAO  extends ManagerDAO<Account> {
    /**
     * @param connection java.sql.Connection to initialize super class
     */
    public AccountManagerDAO(Connection connection) {
        super(connection);
    }

    /**
     * @return Account entity with default fields values
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    public abstract Account createEmptyAccount() throws DAOTechnicalException;

    /**
     * @param accountNumber Account id in database
     * @param rechargeSum Sum to be recharged
     * @param loanSum Loan sum (if exist)
     * @return Updated Account
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    public abstract Account recharge(int accountNumber, BigDecimal rechargeSum, BigDecimal loanSum) throws DAOTechnicalException;

    /**
     * @param accountNumber Account id in database
     * @param sum Loan sum
     * @return Updated Account
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    public abstract Account takeLoan(int accountNumber, BigDecimal sum) throws DAOTechnicalException;

    /**
     * @param accountNumber Account id in database
     * @param sum Withdraw sum
     * @return Updated Account
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    public abstract Account withdraw(int accountNumber, BigDecimal sum) throws DAOTechnicalException;
}
