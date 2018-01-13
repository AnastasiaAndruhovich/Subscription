package by.andruhovich.subscription.dao;

import by.andruhovich.subscription.entity.Block;
import by.andruhovich.subscription.exception.DAOTechnicalException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class BlockDAO extends BaseDAO<Block> {
    private static final String INSERT_BLOCK = "INSERT INTO block (user_id, admin_id, date) VALUES(?, ?, ?)";
    private static final String SELECT_LAST_INSERT_ID = "SELECT LAST_INSERT_ID()";
    private static final String DELETE_BLOCK_BY_USER_ID = "DELETE FROM block WHERE user_id = ?";
    private static final String SELECT_BLOCK_BY_ID = "SELECT * FROM block WHERE user_id = ?";
    private static final String SELECT_ALL_BLOCKS = "SELECT * FROM block";
    private static final String UPDATE_BLOCK = "UPDATE users SET user_id = ?, admin_id = ?, date = ?";

    public BlockDAO(Connection connection) {
        super(connection);
    }

    @Override
    public int create(Block entity) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(INSERT_BLOCK);
            preparedStatement = fillOutStatementByBlock(preparedStatement, entity);
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
            int userId = entity.getUserId();
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
    public Block findEntityById(int id) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;
        List<Block> blocks;

        try {
            preparedStatement = connection.prepareStatement(SELECT_BLOCK_BY_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            blocks = createBlockList(resultSet);
            return blocks.get(0);
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
            blocks = createBlockList(resultSet);
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

        try {
            preparedStatement = connection.prepareStatement(UPDATE_BLOCK);
            preparedStatement = fillOutStatementByBlock(preparedStatement, entity);
            preparedStatement.executeQuery();
            return true;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        } finally {
            close(preparedStatement);
        }
    }

    private List<Block> createBlockList(ResultSet resultSet) throws DAOTechnicalException {
        List<Block> blocks = new LinkedList<>();
        Block block;
        try {
            while (resultSet.next()) {
                int userId = resultSet.getInt("user_id");
                int adminId = resultSet.getInt("admin_id");
                java.util.Date date = new java.util.Date(resultSet.getDate("date").getTime());
                block = new Block(userId, adminId, date);
                blocks.add(block);
            }
            return blocks;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        }
    }

    private PreparedStatement fillOutStatementByBlock(PreparedStatement preparedStatement, Block block)
            throws DAOTechnicalException {
        java.sql.Date date = new java.sql.Date(block.getDate().getTime());

        try {
            preparedStatement.setInt(1, block.getUserId());
            preparedStatement.setInt(2, block.getAdminId());
            preparedStatement.setDate(3, date);
            return preparedStatement;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        }
    }
}
