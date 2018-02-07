package by.andruhovich.subscription.dao.impl;

import by.andruhovich.subscription.dao.BlockManagerDAO;
import by.andruhovich.subscription.entity.Block;
import by.andruhovich.subscription.entity.User;
import by.andruhovich.subscription.exception.DAOTechnicalException;
import by.andruhovich.subscription.mapper.BlockMapper;
import by.andruhovich.subscription.mapper.UserMapper;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
            "a.login admin_login, a.password admin_password FROM users a) a USING (admin_id) ORDER BY admin_id DESC LIMIT ?, ?";
    private static final String UPDATE_BLOCK = "UPDATE users SET user_id = ?, admin_id = ?, date = ?";

    private static final String SELECT_USERS_BY_ADMIN_ID = "SELECT  u.user_id, u.lastname, u.firstname, u.birthdate, " +
            "u.address, u.city, u.postal_index, u.login, u.password FROM block JOIN users u USING (user_id) " +
            "WHERE admin_id = ? ORDER BY admin_id DESC LIMIT ?, ?";
    private static final String SELECT_ADMIN_BY_USER_ID = "SELECT  u.user_id, u.lastname, u.firstname, u.birthdate, " +
            "u.address, u.city, u.postal_index, u.login, u.password FROM block " +
            "JOIN " +
            "(SELECT u.user_id, u.lastname, u.firstname, u.birthdate, u.address, u.city, u.postal_index, u.login, " +
            "u.password FROM users u) u ON (block.admin_id = u.user_id) " +
            "WHERE block.user_id = ?";
    private static final String SELECT_BLOCK_BY_ID = "SELECT user_id, lastname, firstname, birthdate ,address, city, " +
            "postal_index, login, admin_id, admin_lastname, admin_firstname, admin_birthdate, admin_address, " +
            "admin_city, admin_postal_index, admin_login, date FROM block " +
            "JOIN " +
            "(SELECT u.user_id, u.lastname, u.firstname, u.birthdate, u.address, u.city, u.postal_index, " +
            "u.login, u.password FROM users u) u USING (user_id) " +
            "JOIN " +
            "(SELECT a.user_id admin_id, a.lastname admin_lastname, a.firstname admin_firstname, a.birthdate " +
            "admin_birthdate, a.address admin_address, a.city admin_city, a.postal_index admin_postal_index, " +
            "a.login admin_login, a.password admin_password FROM users a) a USING (admin_id) WHERE u.user_id = ?";
    private static final String SELECT_COUNT = "SELECT COUNT(user_id) AS count FROM block";

    private static final Logger LOGGER = LogManager.getLogger(BlockDAO.class);

    public BlockDAO(Connection connection) {
        super(connection);
    }

    @Override
    public int create(Block entity) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for create block");
        PreparedStatement preparedStatement = null;
        BlockMapper mapper = new BlockMapper();

        try {
            preparedStatement = connection.prepareStatement(INSERT_BLOCK);
            preparedStatement = mapper.mapEntityToPreparedStatement(preparedStatement, entity);
            preparedStatement.executeUpdate();
            LOGGER.log(Level.INFO, "Request for create block - succeed");
            return 0;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public boolean delete(int userId) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for delete block");
        return delete(userId, DELETE_BLOCK_BY_USER_ID);
    }

    @Override
    public List<Block> findAll(int startIndex, int endIndex) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for find all");
        List<Block> blocks;
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SELECT_ALL_BLOCKS);
            preparedStatement.setInt(1, startIndex);
            preparedStatement.setInt(2, endIndex);
            ResultSet resultSet = preparedStatement.executeQuery();
            BlockMapper mapper = new BlockMapper();
            blocks = mapper.mapResultSetToEntity(resultSet);
            LOGGER.log(Level.INFO, "Request for find all - succeed");
            return blocks;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public boolean update(Block entity) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for update block");
        PreparedStatement preparedStatement = null;
        BlockMapper mapper = new BlockMapper();

        try {
            preparedStatement = connection.prepareStatement(UPDATE_BLOCK);
            preparedStatement = mapper.mapEntityToPreparedStatement(preparedStatement, entity);
            preparedStatement.executeUpdate();
            LOGGER.log(Level.INFO, "Request for update block - succeed");
            return true;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public List<User> findUsersByAdminId(int id, int startIndex, int endIndex) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for find user by admin id");
        PreparedStatement preparedStatement = null;
        List<User> users;

        try {
            preparedStatement = connection.prepareStatement(SELECT_USERS_BY_ADMIN_ID);
            preparedStatement.setInt(1, id);
            preparedStatement.setInt(2, startIndex);
            preparedStatement.setInt(3, endIndex);
            ResultSet resultSet = preparedStatement.executeQuery();
            UserMapper userMapper = new UserMapper();
            users = userMapper.mapResultSetToEntity(resultSet);
            LOGGER.log(Level.INFO, "Request for find user by admin id - succeed");
            return users;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public User findAdminByUserId(int id) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for find admin by user id");
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SELECT_ADMIN_BY_USER_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            UserMapper userMapper = new UserMapper();
            List<User> users = userMapper.mapResultSetToEntity(resultSet);
            LOGGER.log(Level.INFO, "Request for find admin by user id - succeed");
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
        LOGGER.log(Level.INFO, "Request for delete block by user id");
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(DELETE_BLOCK_BY_USER_ID);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            LOGGER.log(Level.INFO, "Request for delete block by user id - succeed");
            return true;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public Block findEntityById(int id) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for find block by id");
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SELECT_BLOCK_BY_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            BlockMapper blockMapper = new BlockMapper();
            List<Block> blocks = blockMapper.mapResultSetToEntity(resultSet);
            LOGGER.log(Level.INFO, "Request for find block by id - succeed");
            return blocks.get(0);
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
