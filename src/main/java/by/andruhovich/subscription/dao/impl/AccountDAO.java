package by.andruhovich.subscription.dao.impl;

import by.andruhovich.subscription.dao.AccountManagerDAO;
import by.andruhovich.subscription.entity.Account;
import by.andruhovich.subscription.exception.DAOTechnicalException;
import by.andruhovich.subscription.mapper.AccountMapper;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Concrete DAO extends AccountManagerDAO
 */
public class AccountDAO extends AccountManagerDAO {
    private static final String INSERT_ACCOUNT= "INSERT INTO accounts(balance, loan) VALUES (?, ?)";
    private static final String SELECT_LAST_INSERT_ID = "SELECT account_number FROM accounts ORDER BY account_number " +
            "DESC LIMIT 1";
    private static final String DELETE_ACCOUNT_BY_ACCOUNT_NUMBER = "DELETE FROM accounts WHERE account_number = ?";
    private static final String SELECT_COUNT = "SELECT COUNT(account_number) AS count FROM accounts";
    private static final String SELECT_ACCOUNT_BY_ACCOUNT_NUMBER = "SELECT * FROM accounts WHERE account_number = ?";
    private static final String SELECT_ALL_ACCOUNTS = "SELECT * FROM accounts ORDER BY account_number DESC LIMIT ?, ?";
    private static final String UPDATE_ACCOUNT = "UPDATE accounts SET balance = ?, loan = ? WHERE account_number = ?";

    private static final String RECHARGE = "UPDATE accounts SET balance = balance + ?, loan = loan - ? WHERE account_number = ?";
    private static final String TAKE_LOAN = "UPDATE accounts SET balance = balance + ?, loan = loan + ? WHERE account_number = ?";
    private static final String WITHDRAW = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?";

    private static final Logger LOGGER = LogManager.getLogger(AccountDAO.class);

    /**
     * @param connection java.sql.Connection to initialize super class
     */
    public AccountDAO(Connection connection) {
        super(connection);
    }

    /**
     * @param entity Entity to be set in database
     * @return The entity id in database
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    @Override
    public int create(Account entity) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for create account");

        PreparedStatement preparedStatement = null;
        PreparedStatement statement = null;
        AccountMapper mapper = new AccountMapper();
        int id = -1;

        try {
            preparedStatement = connection.prepareStatement(INSERT_ACCOUNT);
            preparedStatement = mapper.mapEntityToPreparedStatement(preparedStatement, entity);
            preparedStatement.executeUpdate();
            statement = connection.prepareStatement(SELECT_LAST_INSERT_ID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                id = resultSet.getInt("account_number");
            }
            LOGGER.log(Level.INFO, "Request for create account - succeed");
            return id;
        } catch (MySQLIntegrityConstraintViolationException e) {
            LOGGER.log(Level.INFO, "Account is already exist");
            return id;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
            close(statement);
        }
    }

    /**
     * @param accountNumber Entity id to be deleted from database
     * @return {@code true} if the operation has been completed successfully
     *         {@code false} otherwise
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */

    @Override
    public boolean delete(int accountNumber) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for delete account");
        return delete(accountNumber, DELETE_ACCOUNT_BY_ACCOUNT_NUMBER);
    }

    /**
     * @param id Entity id to be found in database
     * @return Entity extends T from database
     * @throws DAOTechnicalException
     *              If there was an error during query execute
     */
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
            LOGGER.log(Level.INFO, "Request for find entity by id - succeed");
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

    /**
     * @param startIndex Entity start index in database
     * @param endIndex Entity end index in database
     * @return Entity List from database
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
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

    /**
     * @param  entity Entity to be updated in database
     * @return {@code true} if the operation has been completed successfully
     *         {@code false} otherwise
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    @Override
    public boolean update(Account entity) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for update account");

        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(UPDATE_ACCOUNT);
            AccountMapper mapper = new AccountMapper();
            preparedStatement = mapper.mapEntityToPreparedStatement(preparedStatement, entity);
            preparedStatement.executeUpdate();
            LOGGER.log(Level.INFO, "Request for update account - succeed");
            return true;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    /**
     * @return Account entity with default fields values
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    @Override
    public Account createEmptyAccount() throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for create empty account");

        PreparedStatement preparedStatement = null;
        int id = -1;
        PreparedStatement statement = null;

        try {
            preparedStatement = connection.prepareStatement(INSERT_ACCOUNT);
            BigDecimal sum = new BigDecimal("0.00");
            preparedStatement.setBigDecimal(1, sum);
            preparedStatement.setBigDecimal(2, sum);
            preparedStatement.executeUpdate();
            statement = connection.prepareStatement(SELECT_LAST_INSERT_ID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                id = resultSet.getInt("account_number");
            }
            LOGGER.log(Level.INFO, "Request for create empty account - succeed");
            return findEntityById(id);
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
            close(statement);
        }
    }

    /**
     * @param accountNumber Account id in database
     * @param rechargeSum Sum to be recharged
     * @param loanSum Loan sum (if exist)
     * @return Updated Account
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    @Override
    public Account recharge(int accountNumber, BigDecimal rechargeSum, BigDecimal loanSum) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for sumBalance");
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(RECHARGE);
            preparedStatement.setBigDecimal(1, rechargeSum);
            preparedStatement.setBigDecimal(2, loanSum);
            preparedStatement.setInt(3, accountNumber);
            preparedStatement.executeUpdate();
            LOGGER.log(Level.INFO, "Request for sumBalance - succeed");
            return findEntityById(accountNumber);
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    /**
     * @param accountNumber Account id in database
     * @param sum Loan sum
     * @return Updated Account
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    @Override
    public Account takeLoan(int accountNumber, BigDecimal sum) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for take loan");
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(TAKE_LOAN);
            preparedStatement.setBigDecimal(1, sum);
            preparedStatement.setBigDecimal(2, sum);
            preparedStatement.setInt(3, accountNumber);
            preparedStatement.executeUpdate();
            LOGGER.log(Level.INFO, "Request for take loan - succeed");
            return findEntityById(accountNumber);
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    /**
     * @param accountNumber Account id in database
     * @param sum Withdraw sum
     * @return Updated Account
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    @Override
    public Account withdraw(int accountNumber, BigDecimal sum) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for withdraw");
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(WITHDRAW);
            preparedStatement.setBigDecimal(1, sum);
            preparedStatement.setInt(2, accountNumber);
            preparedStatement.executeUpdate();
            LOGGER.log(Level.INFO, "Request for withdraw - succeed");
            return findEntityById(accountNumber);
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    /**
     * @return Entity extends T count in database
     * @throws DAOTechnicalException
     *          If there was an error during query execute
     */
    public int findEntityCount() throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for get count");
        return findEntityCount(SELECT_COUNT);
    }
}
