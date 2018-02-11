package by.andruhovich.subscription.mapper;

import by.andruhovich.subscription.entity.User;
import by.andruhovich.subscription.exception.DAOTechnicalException;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Provides methods to prepare User entity for setting and getting from database
 */
public class UserMapper implements EntityMapper<User> {
    /**
     * @param resultSet java.sql.ResultSet from database to map on entity
     * @return User list from resultSet
     * @throws DAOTechnicalException
     *          If there was an error during mapping resultSet
     */
    @Override
    public List<User> mapResultSetToEntity(ResultSet resultSet) throws DAOTechnicalException {
        List<User> users = new LinkedList<>();
        User user;
        try {
            while (resultSet.next()) {
                int userId = resultSet.getInt("user_id");
                String firstName = resultSet.getString("firstname");
                String lastName = resultSet.getString("lastname");
                Date birthDate = resultSet.getDate("birthdate");
                String address = resultSet.getString("address");
                String city = resultSet.getString("city");
                int postalIndex = resultSet.getInt("postal_index");
                String login = resultSet.getString("login");
                String password = resultSet.getString("password");
                user = new User(userId, firstName, lastName, birthDate, address, city, postalIndex, login, password);
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        }
    }

    /**
     * @param preparedStatement java.sql.Statement with all necessary parameters
     * @param entity User to be set in database
     * @return Filled out statement by entity
     * @throws DAOTechnicalException
     *          If there was an error during mapping resultSet
     */
    @Override
    public PreparedStatement mapEntityToPreparedStatement(PreparedStatement preparedStatement, User entity) throws DAOTechnicalException {
        Date birthDate = new Date(entity.getBirthDate().getTime());

        try {
            preparedStatement.setInt(1, entity.getRole().getRoleId());
            preparedStatement.setString(2, entity.getFirstName());
            preparedStatement.setString(3, entity.getLastName());
            preparedStatement.setDate(4, birthDate);
            preparedStatement.setString(5, entity.getAddress());
            preparedStatement.setString(6, entity.getCity());
            preparedStatement.setInt(7, entity.getPostalIndex());
            preparedStatement.setInt(8, entity.getAccount().getAccountNumber());
            preparedStatement.setString(9, entity.getLogin());
            preparedStatement.setString(10, entity.getPassword());
            return preparedStatement;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        }
    }
}
