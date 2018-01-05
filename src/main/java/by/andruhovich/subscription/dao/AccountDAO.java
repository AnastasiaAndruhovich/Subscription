package by.andruhovich.subscription.dao;

import by.andruhovich.subscription.entity.Account;
import by.andruhovich.subscription.exception.DAOTechnicalException;

import java.math.BigDecimal;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class AccountDAO extends AccountManagerDAO{
    private static final String INSERT_ACCOUNT= "INSERT INTO accounts(balance, credit) VALUES (?, ?)";
    private static final String SELECT_LAST_INSERT_ID = "SELECT LAST_INSERT_ID()";
    private static final String DELETE_ACCOUNT_BY_ACCOUNT_NUMBER = "DELETE accounts WHERE account_number = ?";
    private static final String SELECT_ACCOUNT_BY_ACCOUNT_NUMBER = "SELECT * FROM accounts WHERE account_number = ?";
    private static final String SELECT_ALL_ACCOUNTS = "SELECT * FROM accounts";
    private static final String UPDATE_ACCOUNT = "UPDATE accounts SET balance = ?, credit = ?";

    public AccountDAO(Connection connection) {
        super(connection);
    }

    @Override
    public int create(Account entity) {
        PreparedStatement preparedStatement;

        try {
            preparedStatement = connection.prepareStatement(INSERT_ACCOUNT);
            preparedStatement.setBigDecimal(1, entity.getBalance());
            preparedStatement.setBigDecimal(2, entity.getCredit());
            preparedStatement.executeQuery();
            preparedStatement = connection.prepareStatement(SELECT_LAST_INSERT_ID);
            ResultSet resultSet = preparedStatement.executeQuery();
            int id = resultSet.getInt("account_number");
            return id;
        } catch (SQLException e) {
            //TODO log
            return -1;
        }
    }

    @Override
    public boolean delete(int id) {
        PreparedStatement preparedStatement;

        try {
            preparedStatement = connection.prepareStatement(DELETE_ACCOUNT_BY_ACCOUNT_NUMBER);
            preparedStatement.setInt(1, id);
            preparedStatement.executeQuery();
            return true;
        } catch (SQLException e) {
            //TODO log
            return false;
        }
    }

    @Override
    public boolean delete(Account entity) {
        PreparedStatement preparedStatement;

        try {
            preparedStatement = connection.prepareStatement(DELETE_ACCOUNT_BY_ACCOUNT_NUMBER);
            preparedStatement.setInt(1, entity.getAccountNumber());
            preparedStatement.executeQuery();
            return true;
        } catch (SQLException e) {
            //TODO log
            return false;
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
    public boolean update(Account entity) {
        PreparedStatement preparedStatement;

        try {
            preparedStatement = connection.prepareStatement(UPDATE_ACCOUNT);
            preparedStatement = fillOutStatementByAccount(preparedStatement, entity);
            preparedStatement.executeQuery();
            return true;
        } catch (SQLException e) {
            //TODO log
            return false;
        } catch (DAOTechnicalException e) {
            //TODO log
            return false;
        }
    }

    @Override
    public void close(Statement statement) {
        super.close(statement);
    }

    private List<Account> createAccountList(ResultSet resultSet) throws DAOTechnicalException {
        List<Account> accounts = new LinkedList<>();
        Account account;
        try {
            while (resultSet.next()) {
                int accountNumber = resultSet.getInt("account_number");
                BigDecimal balance = resultSet.getBigDecimal("balance");
                BigDecimal credit = resultSet.getBigDecimal("credit");
                account = new Account(accountNumber, balance, credit);
                accounts.add(account);
            }
            return accounts;
        } catch (SQLException e) {
            //TODO log
            throw new DAOTechnicalException(e.getMessage());
        }
    }

    private PreparedStatement fillOutStatementByAccount(PreparedStatement preparedStatement, Account account)
            throws DAOTechnicalException {
        try {
            preparedStatement.setBigDecimal(1, account.getBalance());
            preparedStatement.setBigDecimal(2, account.getCredit());
            return preparedStatement;
        } catch (SQLException e) {
            //TODO log
            throw new DAOTechnicalException(e.getMessage());
        }
    }
}
