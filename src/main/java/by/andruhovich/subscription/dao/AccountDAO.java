package by.andruhovich.subscription.dao;

import by.andruhovich.subscription.entity.Account;
import by.andruhovich.subscription.exception.DAOTechnicalException;

import java.math.BigDecimal;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class AccountDAO extends AccountManagerDAO{
    private static final String INSERT_ACCOUNT= "INSERT INTO accounts(balance, loan) VALUES (?, ?)";
    private static final String SELECT_LAST_INSERT_ID = "SELECT LAST_INSERT_ID()";
    private static final String DELETE_ACCOUNT_BY_ACCOUNT_NUMBER = "DELETE FROM accounts WHERE account_number = ?";
    private static final String SELECT_ACCOUNT_BY_ACCOUNT_NUMBER = "SELECT * FROM accounts WHERE account_number = ?";
    private static final String SELECT_ALL_ACCOUNTS = "SELECT * FROM accounts";
    private static final String UPDATE_ACCOUNT = "UPDATE accounts SET balance = ?, loan = ? WHERE account_number = ?";

    private static final String SELECT_BALANCE_BY_ID = "SELECT balance FROM accounts WHERE account_number = ?";
    private static final String SELECT_LOAN_BY_ID = "SELECT loan FROM accounts WHERE account_number = ?";

    private static final String UPDATE_BALANCE = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
    private static final String UPDATE_LOAN = "UPDATE accounts SET loan = loan + ? WHERE account_number = ?";

    public AccountDAO(Connection connection) {
        super(connection);
    }

    @Override
    public int create(Account entity) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(INSERT_ACCOUNT);
            preparedStatement = fillOutStatementByAccount(preparedStatement, entity);
            preparedStatement.executeQuery();
            preparedStatement = connection.prepareStatement(SELECT_LAST_INSERT_ID);
            ResultSet resultSet = preparedStatement.executeQuery();
            int id = resultSet.getInt("account_number");
            return id;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public boolean delete(Account entity) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(DELETE_ACCOUNT_BY_ACCOUNT_NUMBER);
            preparedStatement.setInt(1, entity.getAccountNumber());
            preparedStatement.executeQuery();
            return true;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public Account findEntityById(int id) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;
        List<Account> accounts;

        try {
            preparedStatement = connection.prepareStatement(SELECT_ACCOUNT_BY_ACCOUNT_NUMBER);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            accounts = createAccountList(resultSet);
            return accounts.get(0);
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public List<Account> findAll() throws DAOTechnicalException {
        List<Account> accounts;
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SELECT_ALL_ACCOUNTS);
            ResultSet resultSet = preparedStatement.executeQuery();
            accounts = createAccountList(resultSet);
            return accounts;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public boolean update(Account entity) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(UPDATE_ACCOUNT);
            preparedStatement = fillOutStatementByAccount(preparedStatement, entity);
            preparedStatement.executeQuery();
            return true;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        } finally {
            close(preparedStatement);
        }
    }

    private List<Account> createAccountList(ResultSet resultSet) throws DAOTechnicalException {
        List<Account> accounts = new LinkedList<>();
        Account account;
        try {
            while (resultSet.next()) {
                int accountNumber = resultSet.getInt("account_number");
                BigDecimal balance = resultSet.getBigDecimal("balance");
                BigDecimal loan = resultSet.getBigDecimal("credit");
                account = new Account(accountNumber, balance, loan);
                accounts.add(account);
            }
            return accounts;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        }
    }

    private PreparedStatement fillOutStatementByAccount(PreparedStatement preparedStatement, Account account)
            throws DAOTechnicalException {
        try {
            preparedStatement.setBigDecimal(1, account.getBalance());
            preparedStatement.setBigDecimal(2, account.getLoan());
            preparedStatement.setInt(3, account.getAccountNumber());
            return preparedStatement;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        }
    }

    @Override
    public BigDecimal findBalanceById(int accountNumber) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SELECT_BALANCE_BY_ID);
            preparedStatement.setInt(1, accountNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            BigDecimal balance = resultSet.getBigDecimal("balance");
            return balance;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public BigDecimal findLoanById(int accountNumber) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SELECT_LOAN_BY_ID);
            preparedStatement.setInt(1, accountNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            BigDecimal loan = resultSet.getBigDecimal("loan");
            return loan;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public BigDecimal recharge(int accountNumber, BigDecimal sum) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(UPDATE_BALANCE);
            preparedStatement.setBigDecimal(1, sum);
            preparedStatement.setInt(2, accountNumber);
            preparedStatement.executeQuery();
            return findBalanceById(accountNumber);
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public BigDecimal withdraw(int accountNumber, BigDecimal sum) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(UPDATE_BALANCE);
            preparedStatement.setBigDecimal(1, sum.negate());
            preparedStatement.setInt(2, accountNumber);
            preparedStatement.executeQuery();
            return findBalanceById(accountNumber);
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public BigDecimal takeTheLoan(int accountNumber, BigDecimal sum) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(UPDATE_LOAN);
            preparedStatement.setBigDecimal(1, sum);
            preparedStatement.setInt(2, accountNumber);
            preparedStatement.executeQuery();
            return findLoanById(accountNumber);
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public BigDecimal repayTheLoan(int accountNumber, BigDecimal sum) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(UPDATE_LOAN);
            preparedStatement.setBigDecimal(1, sum.negate());
            preparedStatement.setInt(2, accountNumber);
            preparedStatement.executeQuery();
            return findLoanById(accountNumber);
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        } finally {
            close(preparedStatement);
        }
    }
}
