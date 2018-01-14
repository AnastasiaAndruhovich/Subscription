package by.andruhovich.subscription.dao;

import by.andruhovich.subscription.entity.Block;
import by.andruhovich.subscription.exception.DAOTechnicalException;
import by.andruhovich.subscription.mapper.BlockMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class BlockDAO extends ManagerDAO<Block> {
    private static final String INSERT_BLOCK = "INSERT INTO block (user_id, admin_id, date) VALUES(?, ?, ?)";
    private static final String DELETE_BLOCK_BY_USER_ID = "DELETE FROM block WHERE user_id = ?";
    private static final String SELECT_ALL_BLOCKS = "SELECT * FROM block";
    private static final String UPDATE_BLOCK = "UPDATE users SET user_id = ?, admin_id = ?, date = ?";

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
            throw new DAOTechnicalException(e.getMessage());
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
            throw new DAOTechnicalException(e.getMessage());
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
            throw new DAOTechnicalException(e.getMessage());
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
            throw new DAOTechnicalException(e.getMessage());
        } finally {
            close(preparedStatement);
        }
    }

}
