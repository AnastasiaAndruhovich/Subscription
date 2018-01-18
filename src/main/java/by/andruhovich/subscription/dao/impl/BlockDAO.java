package by.andruhovich.subscription.dao.impl;

import by.andruhovich.subscription.dao.BlockManagerDAO;
import by.andruhovich.subscription.dao.ManagerDAO;
import by.andruhovich.subscription.entity.Block;
import by.andruhovich.subscription.entity.User;
import by.andruhovich.subscription.exception.DAOTechnicalException;
import by.andruhovich.subscription.mapper.BlockMapper;
import by.andruhovich.subscription.mapper.UserMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class BlockDAO extends BlockManagerDAO {
    private static final String INSERT_BLOCK = "INSERT INTO block (user_id, admin_id, date) VALUES(?, ?, ?)";
    private static final String DELETE_BLOCK_BY_USER_ID = "DELETE FROM block WHERE user_id = ?";
    private static final String SELECT_ALL_BLOCKS = "SELECT user_id, lastname, firstname, birthdate ,address, city, " +
            "postal_index, login, admin_id, admin_lastname, admin_firstname, admin_birthdate, admin_address, " +
            "admin_city, admin_postal_index, admin_login, date FROM block " +
            "JOIN " +
            "(SELECT u.user_id, u.lastname, u.firstname, u.birthdate, u.address, u.city, u.postal_index, " +
            "u.login, u.password FROM users u) u USING (user_id) " +
            "JOIN " +
            "(SELECT a.user_id admin_id, a.lastname admin_lastname, a.firstname admin_firstname, a.birthdate " +
            "admin_birthdate, a.address admin_address, a.city admin_city, a.postal_index admin_postal_index, " +
            "a.login admin_login, a.password admin_password FROM users a) a USING (admin_id)";
    private static final String UPDATE_BLOCK = "UPDATE users SET user_id = ?, admin_id = ?, date = ?";

    private static final String SELECT_USERS_BY_ADMIN_ID = "SELECT  u.user_id, u.lastname, u.firstname, u.birthdate, " +
            "u.address, u.city, u.postal_index, u.login, u.password FROM block JOIN users u USING (user_id) " +
            "WHERE admin_id = ?";
    private static final String SELECT_ADMIN_BY_USER_ID = "SELECT  u.user_id, u.lastname, u.firstname, u.birthdate, " +
            "u.address, u.city, u.postal_index, u.login, u.password FROM block " +
            "JOIN " +
            "(SELECT u.user_id, u.lastname, u.firstname, u.birthdate, u.address, u.city, u.postal_index, u.login, " +
            "u.password FROM users u) u ON (block.admin_id = u.user_id) " +
            "WHERE block.user_id = ?";

    public BlockDAO(Connection connection) {
        super(connection);
    }

    @Override
    public int create(Block entity) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;
        BlockMapper mapper = new BlockMapper();

        try {
            preparedStatement = connection.prepareStatement(INSERT_BLOCK);
            preparedStatement = mapper.mapEntityToPreparedStatement(preparedStatement, entity);
            preparedStatement.executeQuery();
            return 0;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public boolean delete(Block entity) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(DELETE_BLOCK_BY_USER_ID);
            int userId = entity.getUser().getUserId();
            preparedStatement.setInt(1, userId);
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public List<Block> findAll() throws DAOTechnicalException {
        List<Block> blocks;
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SELECT_ALL_BLOCKS);
            ResultSet resultSet = preparedStatement.executeQuery();
            BlockMapper mapper = new BlockMapper();
            blocks = mapper.mapResultSetToEntity(resultSet);
            return blocks;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public boolean update(Block entity) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;
        BlockMapper mapper = new BlockMapper();

        try {
            preparedStatement = connection.prepareStatement(UPDATE_BLOCK);
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
    public List<User> findUsersByAdminId(int id) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;
        List<User> users;

        try {
            preparedStatement = connection.prepareStatement(SELECT_USERS_BY_ADMIN_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            UserMapper userMapper = new UserMapper();
            users = userMapper.mapResultSetToEntity(resultSet);
            return users;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public User findAdminByUserId(int id) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SELECT_ADMIN_BY_USER_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            UserMapper userMapper = new UserMapper();
            List<User> users = userMapper.mapResultSetToEntity(resultSet);
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
    public boolean deleteBlockByUserId(int id) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(DELETE_BLOCK_BY_USER_ID);
            preparedStatement.setInt(1, id);
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }
}
