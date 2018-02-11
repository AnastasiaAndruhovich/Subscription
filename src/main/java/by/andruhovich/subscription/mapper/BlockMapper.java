package by.andruhovich.subscription.mapper;

import by.andruhovich.subscription.entity.Block;
import by.andruhovich.subscription.entity.User;
import by.andruhovich.subscription.exception.DAOTechnicalException;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Provides methods to prepare Block entity for setting and getting from database
 */
public class BlockMapper implements EntityMapper<Block> {
    /**
     * @param resultSet java.sql.ResultSet from database to map on entity
     * @return Block list from resultSet
     * @throws DAOTechnicalException
     *          If there was an error during mapping resultSet
     */
    @Override
    public List<Block> mapResultSetToEntity(ResultSet resultSet) throws DAOTechnicalException {
        List<User> users;
        List<User> admins = new LinkedList<>();
        List<java.util.Date> dates = new LinkedList<>();
        List<Block> blocks = new LinkedList<>();

        UserMapper userMapper = new UserMapper();
        users = userMapper.mapResultSetToEntity(resultSet);

        try {
            while (resultSet.next()) {
                int userId = resultSet.getInt("admin_id");
                String firstName = resultSet.getString("admin_firstname");
                String lastName = resultSet.getString("admin_lastname");
                Date birthDate = resultSet.getDate("admin_birthdate");
                String address = resultSet.getString("admin_address");
                String city = resultSet.getString("admin_city");
                int postalIndex = resultSet.getInt("admin_postal_index");
                String login = resultSet.getString("admin_login");
                String password = resultSet.getString("admin_password");
                java.util.Date date = new java.util.Date(resultSet.getDate("date").getTime());
                dates.add(date);
                User admin = new User(userId, firstName, lastName, birthDate, address, city, postalIndex, login, password);
                admins.add(admin);
            }

            if (!users.isEmpty() && users.size() == admins.size() && users.size() == dates.size()) {
                int i = 0;
                while (i < admins.size()) {
                    Block block = new Block(users.get(i), admins.get(i), dates.get(i));
                    blocks.add(block);
                    i++;
                }
            }
            return blocks;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        }
    }

    /**
     * @param preparedStatement java.sql.Statement with all necessary parameters
     * @param entity Block to be set in database
     * @return Filled out statement by entity
     * @throws DAOTechnicalException
     *          If there was an error during mapping resultSet
     */
    @Override
    public PreparedStatement mapEntityToPreparedStatement(PreparedStatement preparedStatement, Block entity) throws DAOTechnicalException {
        Date date = new Date(entity.getDate().getTime());

        try {
            preparedStatement.setInt(1, entity.getUser().getUserId());
            preparedStatement.setInt(2, entity.getAdmin().getUserId());
            preparedStatement.setDate(3, date);
            return preparedStatement;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        }
    }
}
