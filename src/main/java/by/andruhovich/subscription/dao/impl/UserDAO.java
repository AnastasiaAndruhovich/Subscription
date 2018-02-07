package by.andruhovich.subscription.dao.impl;

import by.andruhovich.subscription.dao.UserManagerDAO;
import by.andruhovich.subscription.entity.Account;
import by.andruhovich.subscription.entity.Role;
import by.andruhovich.subscription.entity.User;
import by.andruhovich.subscription.exception.DAOTechnicalException;
import by.andruhovich.subscription.mapper.AccountMapper;
import by.andruhovich.subscription.mapper.RoleMapper;
import by.andruhovich.subscription.mapper.UserMapper;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class UserDAO extends UserManagerDAO {
    private static final String SELECT_PASSWORD_BY_ID = "SELECT password FROM users WHERE user_id = ?";
    private static final String SELECT_LAST_INSERT_ID = "SELECT user_id FROM users ORDER BY user_id DESC LIMIT 1";
    private static final String SELECT_LOGIN = "SELECT user_id FROM users WHERE login = ?";
    private static final String INSERT_USER = "INSERT INTO users(role_id, firstname, lastname, birthdate, address, city," +
            " postal_index, account_number, login, password) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String DELETE_USER_BY_ID = "DELETE FROM users WHERE user_id = ?";
    private static final String SELECT_COUNT = "SELECT COUNT(user_id) AS count FROM users";
    private static final String SELECT_USER_BY_ID = "SELECT user_id, lastname, firstname, birthdate, address, city, " +
            "postal_index, login, password FROM users WHERE user_id = ?";
    private static final String SELECT_ALL_USERS = "SELECT user_id, lastname, firstname, birthdate, address, " +
            "city, postal_index, login, password FROM users LIMIT ?, ?";
    private static final String UPDATE_USER = "UPDATE users SET role_id = ?, firstname = ?, lastname = ?, birthdate = ?, " +
            "address = ?, city = ?, postal_index = ?, account_number = ?, login = ?, password = ? WHERE user_id = ?";
    private static final String SELECT_USERS_BY_ROLE_ID = "";

    private static final String SELECT_USER_BY_ACCOUNT_NUMBER = "SELECT user_id, lastname, firstname, birthdate, " +
            "address, city, postal_index, login, password FROM users WHERE account_number = ?";
    private static final String SELECT_USER_BY_LOGIN = "SELECT user_id, lastname, firstname, birthdate, " +
            "address, city, postal_index, login, password FROM users WHERE login = ?";
    private static final String SELECT_ROLE_BY_USER_ID = "SELECT  r.role_id, r.name FROM users " +
            "JOIN roles r USING (role_id) WHERE user_id = ?";
    private static final String SELECT_ACCOUNT_BY_USER_ID = "SELECT a.account_number, a.balance, a.loan FROM users " +
            "JOIN accounts a USING (account_number) WHERE user_id = ?";

    private static final Logger LOGGER = LogManager.getLogger(UserDAO.class);

    public UserDAO(Connection connection) {
        super(connection);
    }

    @Override
    public int create(User user) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for create user");
        PreparedStatement preparedStatement = null;
        PreparedStatement statement = null;
        UserMapper mapper = new UserMapper();
        int id = -1;

        try {
            preparedStatement = connection.prepareStatement(INSERT_USER);
            preparedStatement = mapper.mapEntityToPreparedStatement(preparedStatement, user);
            preparedStatement.executeUpdate();
            statement = connection.prepareStatement(SELECT_LAST_INSERT_ID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                id = resultSet.getInt("user_id");
            }
            LOGGER.log(Level.INFO, "Request for create user - succeed");
            return id;
        } catch (MySQLIntegrityConstraintViolationException e) {
            LOGGER.log(Level.INFO, "User is already exist");
            return id;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
            close(statement);
        }
    }

    @Override
    public boolean delete(int id) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for delete user");
        return delete(id, DELETE_USER_BY_ID);
    }

    @Override
    public User findEntityById(int id) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for find entity by id");
        PreparedStatement preparedStatement = null;
        List<User> users;

        try {
            preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            UserMapper mapper = new UserMapper();
            users = mapper.mapResultSetToEntity(resultSet);
            LOGGER.log(Level.INFO, "Request for find entity by id - succeed");
            if (users.isEmpty()) {
                return null;
            }
            return users.get(0);
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public List<User> findAll(int startIndex, int endIndex) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for find all");
        List<User> users;
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SELECT_ALL_USERS);
            preparedStatement.setInt(1, startIndex);
            preparedStatement.setInt(2, endIndex);
            ResultSet resultSet = preparedStatement.executeQuery();
            UserMapper mapper = new UserMapper();
            users = mapper.mapResultSetToEntity(resultSet);
            LOGGER.log(Level.INFO, "Request for find all - succeed");
            return users;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public boolean update(User entity) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for update user");
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(UPDATE_USER);
            UserMapper mapper = new UserMapper();
            preparedStatement = mapper.mapEntityToPreparedStatement(preparedStatement, entity);
            preparedStatement.setInt(11, entity.getUserId());
            preparedStatement.executeUpdate();
            LOGGER.log(Level.INFO, "Request for update user - succeed");
            return true;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public User findUserByAccountNumber(int accountNumber) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for user by account number");
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SELECT_USER_BY_ACCOUNT_NUMBER);
            preparedStatement.setInt(1, accountNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<User> users = new UserMapper().mapResultSetToEntity(resultSet);
            LOGGER.log(Level.INFO, "Request for user by account number - succeed");
            if (!users.isEmpty()) {
                return users.get(0);
            }
            return null;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public List<User> findUsersByRoleId(int id) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for find users by role id");
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SELECT_USERS_BY_ROLE_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            LOGGER.log(Level.INFO, "Request for find users by role id - succeed");
            UserMapper userMapper = new UserMapper();
            return userMapper.mapResultSetToEntity(resultSet);
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public String findPasswordById(int id) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for find password by id");
        PreparedStatement preparedStatement = null;
        String password = null;

        try {
            preparedStatement = connection.prepareStatement(SELECT_PASSWORD_BY_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                password = resultSet.getString("password");
            }
            LOGGER.log(Level.INFO, "Request for find password by id - succeed");
            return password;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public int findUserIdByLogin(String login) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for is login exist");
        PreparedStatement preparedStatement = null;
        int userId = -1;

        try {
            preparedStatement = connection.prepareStatement(SELECT_LOGIN);
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                userId = resultSet.getInt("user_id");
            }
            LOGGER.log(Level.INFO, "Request for is login exist - succeed");
            return userId;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public Role findRoleByUserId(int id) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for find role by user id");
        PreparedStatement preparedStatement = null;
        List<Role> roles;

        try {
            preparedStatement = connection.prepareStatement(SELECT_ROLE_BY_USER_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            RoleMapper roleMapper = new RoleMapper();
            roles = roleMapper.mapResultSetToEntity(resultSet);
            LOGGER.log(Level.INFO, "Request for find role by user id - succeed");
            if (!roles.isEmpty()) {
                return roles.get(0);
            }
            return null;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public Account findAccountByUserId(int id) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for find account by user id");
        PreparedStatement preparedStatement = null;
        List<Account> accounts;

        try {
            preparedStatement = connection.prepareStatement(SELECT_ACCOUNT_BY_USER_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            AccountMapper accountMapper = new AccountMapper();
            accounts = accountMapper.mapResultSetToEntity(resultSet);
            LOGGER.log(Level.INFO, "Request for find account by user id - succeed");
            if (!accounts.isEmpty()) {
                return accounts.get(0);
            }
            return null;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public User findUserByLogin(String login) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for find user by login");
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SELECT_USER_BY_LOGIN);
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();
            UserMapper userMapper = new UserMapper();
            List<User> users = userMapper.mapResultSetToEntity(resultSet);
            LOGGER.log(Level.INFO, "Request for find user by login - succeed");
            if (!users.isEmpty()) {
                return users.get(0);
            }
            return null;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    public int findEntityCount() throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for get count");
        return findEntityCount(SELECT_COUNT);
    }


}
