package by.andruhovich.subscription.dao.impl;

import by.andruhovich.subscription.dao.UserManagerDAO;
import by.andruhovich.subscription.entity.Account;
import by.andruhovich.subscription.entity.Role;
import by.andruhovich.subscription.entity.Subscription;
import by.andruhovich.subscription.entity.User;
import by.andruhovich.subscription.exception.DAOTechnicalException;
import by.andruhovich.subscription.mapper.AccountMapper;
import by.andruhovich.subscription.mapper.PublicationMapper;
import by.andruhovich.subscription.mapper.RoleMapper;
import by.andruhovich.subscription.mapper.UserMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class UserDAO extends UserManagerDAO {
    private static final String SELECT_PASSWORD_BY_LOGIN = "SELECT password FROM users WHERE login = ?";
    private static final String SELECT_LOGIN = "SELECT COUNT(user_id) FROM users WHERE login = ?";
    private static final String INSERT_USER = "INSERT INTO users(role_id, firstname, lastname, birthdate, address, city," +
            " postal_index, account_number, login, password) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String DELETE_USER_BY_ID = "DELETE FROM users WHERE user_id = ?";
    private static final String SELECT_USER_BY_ID = "SELECT user_id, lastname, firstname, birthdate, address, city, " +
            "postal_index, login, password FROM users WHERE user_id = ?";
    private static final String SELECT_ALL_USERS = "SELECT user_id, lastname, firstname, birthdate, address, " +
            "city, postal_index, login, password FROM users";
    private static final String UPDATE_USER = "UPDATE users SET role_id = ?, firstname = ?, lastname = ?, birthdate = ?, " +
            "address = ?, city = ?, postal_index = ?, account_number = ?, login = ?, password = ? WHERE user_id = ?";
    private static final String SELECT_USERS_BY_ROLE_ID = "";

    private static final String SELECT_USER_BY_ACCOUNT_NUMBER = "";
    private static final String SELECT_ROLE_BY_USER_ID = "SELECT  r.role_id, r.name FROM users " +
            "JOIN roles r USING (role_id) WHERE user_id = ?";
    private static final String SELECT_ACCOUNT_BY_USER_ID = "SELECT a.account_number, a.balance, a.loan FROM users " +
            "JOIN accounts a USING (account_number) WHERE user_id = ?";

    public UserDAO(Connection connection) {
        super(connection);
    }

    @Override
    public int create(User user) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;
        UserMapper mapper = new UserMapper();
        int id = -1;

        try {
            preparedStatement = connection.prepareStatement(INSERT_USER);
            preparedStatement = mapper.mapEntityToPreparedStatement(preparedStatement, user);
            preparedStatement.executeQuery();
            preparedStatement = connection.prepareStatement(SELECT_LAST_INSERT_ID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                id = resultSet.getInt("user_id");
            }
            return id;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public boolean delete(User entity) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(DELETE_USER_BY_ID);
            preparedStatement.setInt(1, entity.getUserId());
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public User findEntityById(int id) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;
        List<User> users;

        try {
            preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            UserMapper mapper = new UserMapper();
            users = mapper.mapResultSetToEntity(resultSet);
            if (users.isEmpty()) {
                return null;
            }
            return users.get(0);
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public List findAll() throws DAOTechnicalException {
        List<User> users;
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SELECT_ALL_USERS);
            ResultSet resultSet = preparedStatement.executeQuery();
            UserMapper mapper = new UserMapper();
            users = mapper.mapResultSetToEntity(resultSet);
            return users;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public boolean update(User entity) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(UPDATE_USER);
            UserMapper mapper = new UserMapper();
            preparedStatement = mapper.mapEntityToPreparedStatement(preparedStatement, entity);
            preparedStatement.executeQuery();
            return true;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public User findUserByAccountNumber(int id) throws DAOTechnicalException {
        return null;
    }

    @Override
    public List<User> findUsersByRoleId(int id) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SELECT_USERS_BY_ROLE_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            UserMapper userMapper = new UserMapper();
            return userMapper.mapResultSetToEntity(resultSet);
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        } finally {
            close(preparedStatement);
        }
    }

    public String findPasswordByLogin(String login) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;
        String password = null;

        try {
            preparedStatement = connection.prepareStatement(SELECT_PASSWORD_BY_LOGIN);
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                password = resultSet.getString("password");
            }
            return password;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public boolean isLoginExist(String login) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;
        int quantity = -1;

        try {
            preparedStatement = connection.prepareStatement(SELECT_LOGIN);
            preparedStatement.setString(1, login);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                quantity = resultSet.getInt("COUNT(user_id)");
            }
            return quantity == 1;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public Role findRoleByUserId(int id) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;
        List<Role> roles;

        try {
            preparedStatement = connection.prepareStatement(SELECT_ROLE_BY_USER_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            RoleMapper roleMapper = new RoleMapper();
            roles = roleMapper.mapResultSetToEntity(resultSet);
            if (!roles.isEmpty()) {
                return roles.get(0);
            }
            return null;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public Account findAccountByUserId(int id) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;
        List<Account> accounts;

        try {
            preparedStatement = connection.prepareStatement(SELECT_ACCOUNT_BY_USER_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            AccountMapper accountMapper = new AccountMapper();
            accounts = accountMapper.mapResultSetToEntity(resultSet);
            if (!accounts.isEmpty()) {
                return accounts.get(0);
            }
            return null;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public List<Subscription> findSubscriptionsByUserId(int id) throws DAOTechnicalException {
        SubscriptionDAO subscriptionDAO = new SubscriptionDAO(connection);
        return subscriptionDAO.findSubscriptionsByUserId(id);
    }

    @Override
    public User findBlockedAdminByUserId(int id) throws DAOTechnicalException {
        BlockDAO blockDAO = new BlockDAO(connection);
        return blockDAO.findAdminByUserId(id);
    }

    @Override
    public List<User> findBlockedUsersByAdminId(int id) throws DAOTechnicalException {
        BlockDAO blockDAO = new BlockDAO(connection);
        return blockDAO.findUsersByAdminId(id);
    }
}
