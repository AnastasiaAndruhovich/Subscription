package by.andruhovich.subscription.dao.impl;

import by.andruhovich.subscription.dao.AccountManagerDAO;
import by.andruhovich.subscription.entity.Account;
import by.andruhovich.subscription.entity.User;
import by.andruhovich.subscription.exception.DAOTechnicalException;
import by.andruhovich.subscription.mapper.AccountMapper;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    private static final Logger LOGGER = LogManager.getLogger(AccountDAO.class);

    public AccountDAO(Connection connection) {
        super(connection);
    }

    @Override
    public int create(Account entity) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for create account");

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
            LOGGER.log(Level.INFO, "Request for create account - succeed");
            return id;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public boolean delete(int accountNumber) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for delete account");
        return delete(accountNumber, DELETE_ACCOUNT_BY_ACCOUNT_NUMBER);
    }

    @Override
    public Account findEntityById(int id) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for find entity by id");

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
            LOGGER.log(Level.INFO, "Request for find entity by id - succeed");
            return accounts.get(0);
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public List<Account> findAll(int startIndex, int endIndex) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for find all");

        List<Account> accounts;
        PreparedStatement preparedStatement = null;
        AccountMapper mapper = new AccountMapper();

        try {
            preparedStatement = connection.prepareStatement(SELECT_ALL_ACCOUNTS);
            preparedStatement.setInt(1, startIndex);
            preparedStatement.setInt(2, endIndex);
            ResultSet resultSet = preparedStatement.executeQuery();
            accounts = mapper.mapResultSetToEntity(resultSet);
            LOGGER.log(Level.INFO, "Request for find all - succeed");
            return accounts;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public boolean update(Account entity) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for update account");

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(UPDATE_ACCOUNT);
            AccountMapper mapper = new AccountMapper();
            preparedStatement = mapper.mapEntityToPreparedStatement(preparedStatement, entity);
            preparedStatement.executeQuery();
            LOGGER.log(Level.INFO, "Request for update account - succeed");
            return true;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public Account createEmptyAccount() throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for create empty account");

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
            LOGGER.log(Level.INFO, "Request for create empty account - succeed");
            return findEntityById(id);
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public User findUserByAccountNumber(int id) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for find user by account number");
        UserDAO userDAO = new UserDAO(connection);
        return userDAO.findUserByAccountNumber(id);
    }

    @Override
    public BigDecimal findBalanceById(int accountNumber) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for find balance by id");

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SELECT_BALANCE_BY_ID);
            preparedStatement.setInt(1, accountNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            BigDecimal balance = resultSet.getBigDecimal("balance");
            LOGGER.log(Level.INFO, "Request for find balance by id - succeed");
            return balance;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public BigDecimal findLoanById(int accountNumber) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for find loan by id");

        PreparedStatement preparedStatement = null;
        BigDecimal loan = null;

        try {
            preparedStatement = connection.prepareStatement(SELECT_LOAN_BY_ID);
            preparedStatement.setInt(1, accountNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                loan = resultSet.getBigDecimal("loan");
            }
            LOGGER.log(Level.INFO, "Request for find loan by id - succeed");
            return loan;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public BigDecimal recharge(int accountNumber, BigDecimal sum) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for recharge");
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(UPDATE_BALANCE);
            preparedStatement.setBigDecimal(1, sum);
            preparedStatement.setInt(2, accountNumber);
            preparedStatement.executeQuery();
            LOGGER.log(Level.INFO, "Request for recharge - succeed");
            return findBalanceById(accountNumber);
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public BigDecimal withdraw(int accountNumber, BigDecimal sum) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for withdraw");
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(UPDATE_BALANCE);
            preparedStatement.setBigDecimal(1, sum.negate());
            preparedStatement.setInt(2, accountNumber);
            preparedStatement.executeQuery();
            LOGGER.log(Level.INFO, "Request for withdraw - succeed");
            return findBalanceById(accountNumber);
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public BigDecimal takeLoan(int accountNumber, BigDecimal sum) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for take loan");
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
    public BigDecimal repayLoan(int accountNumber, BigDecimal sum) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for repay loan");
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
