package by.andruhovich.subscription.mapper;

import by.andruhovich.subscription.entity.User;
import by.andruhovich.subscription.exception.DAOTechnicalException;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class UserMapper implements EntityMapper<User> {
    @Override
    public List<User> mapResultSetToEntity(ResultSet resultSet) throws DAOTechnicalException {
        List<User> users = new LinkedList<>();
        User user;
        try {
            while (resultSet.next()) {
                int userId = resultSet.getInt("user_id");
                String firstname = resultSet.getString("firstname");
                String lastname = resultSet.getString("lastname");
                Date birthdate = resultSet.getDate("birthdate");
                String address = resultSet.getString("address");
                String city = resultSet.getString("city");
                int postalIndex = resultSet.getInt("postal_index");
                String login = resultSet.getString("login");
                String password = resultSet.getString("password");
                user = new User(userId, firstname, lastname, birthdate, address, city, postalIndex, login, password);
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        }
    }

    @Override
    public PreparedStatement mapEntityToPreparedStatement(PreparedStatement preparedStatement, User entity) throws DAOTechnicalException {
        Date birthdate = new Date(entity.getBirthDate().getTime());

        try {
            preparedStatement.setInt(1, entity.getRole().getRoleId());
            preparedStatement.setString(2, entity.getFirstName());
            preparedStatement.setString(3, entity.getLastName());
            preparedStatement.setDate(4, birthdate);
            preparedStatement.setString(5, entity.getAddress());
            preparedStatement.setString(6, entity.getCity());
            preparedStatement.setInt(7, entity.getPostalIndex());
            preparedStatement.setInt(8, entity.getAccount().getAccountNumber());
            preparedStatement.setString(9, entity.getLogin());
            preparedStatement.setString(10, entity.getPassword());
            preparedStatement.setInt(11, entity.getUserId());
            return preparedStatement;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        }
    }
}
