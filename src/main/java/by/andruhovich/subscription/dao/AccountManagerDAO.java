package by.andruhovich.subscription.dao;

import by.andruhovich.subscription.dao.ManagerDAO;
import by.andruhovich.subscription.entity.Account;
import by.andruhovich.subscription.exception.DAOTechnicalException;

import java.math.BigDecimal;
import java.sql.Connection;

public abstract class AccountManagerDAO extends ManagerDAO<Account> {
    public AccountManagerDAO(Connection connection) {
        super(connection);
    }

    public abstract int createEmptyAccount() throws DAOTechnicalException;

    public abstract BigDecimal findBalanceById(int accountNumber) throws DAOTechnicalException;
    public abstract BigDecimal findLoanById(int accountNumber) throws DAOTechnicalException;

    public abstract BigDecimal recharge(int accountNumber, BigDecimal sum) throws DAOTechnicalException;
    public abstract BigDecimal takeTheLoan(int accountNumber, BigDecimal sum) throws DAOTechnicalException;
    public abstract BigDecimal repayTheLoan(int accountNumber, BigDecimal sum) throws DAOTechnicalException;
    public abstract BigDecimal withdraw(int accountNumber, BigDecimal sum) throws DAOTechnicalException;
}
