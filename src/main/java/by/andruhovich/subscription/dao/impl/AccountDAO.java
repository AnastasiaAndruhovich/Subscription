package by.andruhovich.subscription.dao.impl;

import by.andruhovich.subscription.dao.AccountManagerDAO;
import by.andruhovich.subscription.entity.Account;
import by.andruhovich.subscription.entity.User;
import by.andruhovich.subscription.exception.DAOTechnicalException;
import by.andruhovich.subscription.mapper.AccountMapper;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class AccountDAO extends AccountManagerDAO {
    private static final String INSERT_ACCOUNT= "INSERT INTO accounts(balance, loan) VALUES (?, ?)";
    private static final String INSERT_EMPTY_ACCOUNT= "INSERT INTO accounts() VALUES ()";
    private static final String DELETE_ACCOUNT_BY_ACCOUNT_NUMBER = "DELETE FROM accounts WHERE account_number = ?";
    private static final String SELECT_ACCOUNT_BY_ACCOUNT_NUMBER = "SELECT * FROM accounts WHERE account_number = ?";
    private static final String SELECT_ALL_ACCOUNTS = "SELECT * FROM accounts LIMIT ?, ?";
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
        AccountMapper mapper = new AccountMapper();
        int id = -1;

        try {
            preparedStatement = connection.prepareStatement(INSERT_ACCOUNT);
            preparedStatement = mapper.mapEntityToPreparedStatement(preparedStatement, entity);
            preparedStatement.executeQuery();
            preparedStatement = connection.prepareStatement(SELECT_LAST_INSERT_ID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                id = resultSet.getInt("account_number");
            }
            return id;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public boolean delete(int accountNumber) throws DAOTechnicalException {
        return delete(accountNumber, DELETE_ACCOUNT_BY_ACCOUNT_NUMBER);
    }

    @Override
    public Account findEntityById(int id) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;
        List<Account> accounts;

        try {
            preparedStatement = connection.prepareStatement(SELECT_ACCOUNT_BY_ACCOUNT_NUMBER);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            AccountMapper mapper = new AccountMapper();
            accounts = mapper.mapResultSetToEntity(resultSet);
            if (accounts.isEmpty()) {
                return null;
            }
            return accounts.get(0);
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public List<Account> findAll(int startIndex, int endIndex) throws DAOTechnicalException {
        List<Account> accounts;
        PreparedStatement preparedStatement = null;
        AccountMapper mapper = new AccountMapper();

        try {
            preparedStatement = connection.prepareStatement(SELECT_ALL_ACCOUNTS);
            preparedStatement.setInt(1, startIndex);
            preparedStatement.setInt(2, endIndex);
            ResultSet resultSet = preparedStatement.executeQuery();
            accounts = mapper.mapResultSetToEntity(resultSet);
            return accounts;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public boolean update(Account entity) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(UPDATE_ACCOUNT);
            AccountMapper mapper = new AccountMapper();
            preparedStatement = mapper.mapEntityToPreparedStatement(preparedStatement, entity);
            preparedStatement.executeQuery();
            return true;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public Account createEmptyAccount() throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;
        int id = -1;

        try {
            preparedStatement = connection.prepareStatement(INSERT_EMPTY_ACCOUNT);
            preparedStatement.executeQuery();
            preparedStatement = connection.prepareStatement(SELECT_LAST_INSERT_ID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                id = resultSet.getInt("account_number");
            }
            return findEntityById(id);
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public User findUserByAccountNumber(int id) throws DAOTechnicalException {
        UserDAO userDAO = new UserDAO(connection);
        return userDAO.findUserByAccountNumber(id);
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
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public BigDecimal findLoanById(int accountNumber) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;
        BigDecimal loan = null;

        try {
            preparedStatement = connection.prepareStatement(SELECT_LOAN_BY_ID);
            preparedStatement.setInt(1, accountNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                loan = resultSet.getBigDecimal("loan");
            }
            return loan;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
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
            throw new DAOTechnicalException("Execute statement error. ", e);
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
            throw new DAOTechnicalException("Execute statement error. ", e);
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
            throw new DAOTechnicalException("Execute statement error. ", e);
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
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }
}
