package by.andruhovich.subscription.mapper;

import by.andruhovich.subscription.entity.User;
import by.andruhovich.subscription.exception.DAOTechnicalException;
import by.andruhovich.subscription.mapper.EntityMapper;
import by.andruhovich.subscription.entity.Block;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class BlockMapper implements EntityMapper<Block> {
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
                String firstname = resultSet.getString("admin_firstname");
                String lastname = resultSet.getString("admin_lastname");
                Date birthdate = resultSet.getDate("admin_birthdate");
                String address = resultSet.getString("admin_address");
                String city = resultSet.getString("admin_city");
                int postalIndex = resultSet.getInt("admin_postal_index");
                String login = resultSet.getString("admin_login");
                String password = resultSet.getString("admin_password");
                java.util.Date date = new java.util.Date(resultSet.getDate("date").getTime());
                dates.add(date);
                User admin = new User(userId, firstname, lastname, birthdate, address, city, postalIndex, login, password);
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
