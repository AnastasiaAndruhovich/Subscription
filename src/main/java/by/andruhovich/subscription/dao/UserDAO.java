package by.andruhovich.subscription.dao;

import by.andruhovich.subscription.entity.User;
import by.andruhovich.subscription.exception.DAOTechnicalException;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class UserDAO extends UserManagerDAO {
    private static final String SELECT_PASSWORD_BY_LOGIN = "SELECT password FROM users WHERE login = ?";
    private static final String INSERT_USER = "INSERT INTO users(role_id, firstname, lastname, birthdate, address, city," +
            " postal_index, account_number, login, password) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_LAST_INSERT_ID = "SELECT LAST_INSERT_ID()";
    private static final String DELETE_USER_BY_ID = "DELETE FROM users WHERE user_id = ?";
    private static final String SELECT_USER_BY_ID = "SELECT * FROM users WHERE user_id = ?";
    private static final String SELECT_ALL_USERS = "SELECT * FROM users";
    private static final String UPDATE_USER = "UPDATE users SET role_id = ?, firstname = ?, lastname = ?, birthdate = ?, " +
            "address = ?, city = ?, postal_index = ?, account_number = ?, login = ?, password = ? WHERE user_id = ?";

    @Override
    public int create(User user) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(INSERT_USER);
            preparedStatement = fillOutStatementByUser(preparedStatement, user);
            preparedStatement.executeQuery();
            preparedStatement = connection.prepareStatement(SELECT_LAST_INSERT_ID);
            ResultSet resultSet = preparedStatement.executeQuery();
            int id = resultSet.getInt("user_id");
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
            users = createUserList(resultSet);
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
            users = createUserList(resultSet);
            return users;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public boolean update(User entity) throws DAOTechnicalException {
        PreparedStatement preparedStatement;

        try {
            preparedStatement = connection.prepareStatement(UPDATE_USER);
            preparedStatement = fillOutStatementByUser(preparedStatement, entity);
            preparedStatement.executeQuery();
            return true;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
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

    private PreparedStatement fillOutStatementByUser(PreparedStatement preparedStatement, User user) throws DAOTechnicalException {
        try {
            preparedStatement.setInt(1, user.getRoleId());
            preparedStatement.setString(2, user.getFirstname());
            preparedStatement.setString(3, user.getLastname());
            preparedStatement.setDate(4, user.getBirthdate());
            preparedStatement.setString(5, user.getAddress());
            preparedStatement.setString(6, user.getCity());
            preparedStatement.setInt(7, user.getPostalIndex());
            preparedStatement.setInt(8, user.getAccountNumber());
            preparedStatement.setString(9, user.getLogin());
            preparedStatement.setString(10, user.getPassword());
            preparedStatement.setInt(11, user.getUserId());
            return preparedStatement;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        }
    }

    private List<User> createUserList(ResultSet resultSet) throws DAOTechnicalException {
        List<User> users = new LinkedList<>();
        User user;
        try {
            while (resultSet.next()) {
                int userId = resultSet.getInt("user_id");
                int roleId = resultSet.getInt("role_id");
                String firstname = resultSet.getString("firstname");
                String lastname = resultSet.getString("lastname");
                Date birthdate = resultSet.getDate("birthdate");
                String address = resultSet.getString("address");
                String city = resultSet.getString("city");
                int postalIndex = resultSet.getInt("postal_index");
                int accountNumber = resultSet.getInt("account_number");
                String login = resultSet.getString("login");
                String password = resultSet.getString("password");
                user = new User(userId, roleId, firstname, lastname, birthdate, address, city, postalIndex, accountNumber,
                        login, password);
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        }
    }
}
