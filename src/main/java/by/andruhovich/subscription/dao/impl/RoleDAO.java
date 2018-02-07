package by.andruhovich.subscription.dao.impl;

import by.andruhovich.subscription.dao.RoleManagerDAO;
import by.andruhovich.subscription.entity.Role;
import by.andruhovich.subscription.exception.DAOTechnicalException;
import by.andruhovich.subscription.mapper.RoleMapper;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class RoleDAO extends RoleManagerDAO {
    private static final String SELECT_LAST_INSERT_ID = "SELECT role_id FROM roles ORDER BY role_id DESC LIMIT 1";
    private static final String INSERT_ROLE= "INSERT INTO roles(name) VALUE (?)";
    private static final String DELETE_ROLE_BY_ID = "DELETE FROM roles WHERE role_id = ?";
    private static final String SELECT_ROLE_BY_ID = "SELECT * FROM roles WHERE role_id = ?";
    private static final String SELECT_ALL_ROLES = "SELECT * FROM roles LIMIT ?, ?";
    private static final String UPDATE_ROLE = "UPDATE roles SET name = ? WHERE role_id = ?";
    private static final String SELECT_COUNT = "SELECT COUNT(role_id) AS count FROM roles";

    private static final Logger LOGGER = LogManager.getLogger(RoleDAO.class);

    public RoleDAO(Connection connection) {
        super(connection);
    }

    @Override
    public int create(Role entity) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for create role");
        PreparedStatement preparedStatement = null;
        PreparedStatement statement = null;
        RoleMapper mapper = new RoleMapper();
        int id = 0;

        try {
            preparedStatement = connection.prepareStatement(INSERT_ROLE);
            preparedStatement = mapper.mapEntityToPreparedStatement(preparedStatement, entity);
            preparedStatement.executeUpdate();
            statement = connection.prepareStatement(SELECT_LAST_INSERT_ID);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                id = resultSet.getInt("role_id");
            }
            LOGGER.log(Level.INFO, "Request for create role - succeed");
            return id;
        } catch (MySQLIntegrityConstraintViolationException e) {
            LOGGER.log(Level.INFO, "Role is already exist");
            return id;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
            close(statement);
        }
    }

    @Override
    public boolean delete(int id) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for delete role");
        return delete(id, DELETE_ROLE_BY_ID);
    }

    @Override
    public Role findEntityById(int id) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for find entity by id");
        PreparedStatement preparedStatement = null;
        List<Role> roles;

        try {
            preparedStatement = connection.prepareStatement(SELECT_ROLE_BY_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            RoleMapper mapper = new RoleMapper();
            roles = mapper.mapResultSetToEntity(resultSet);
            LOGGER.log(Level.INFO, "Request for find entity by id - succeed");
            return roles.get(0);
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public List<Role> findAll(int startIndex, int endIndex) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for find all");
        List<Role> roles;
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SELECT_ALL_ROLES);
            preparedStatement.setInt(1, startIndex);
            preparedStatement.setInt(2, endIndex);
            ResultSet resultSet = preparedStatement.executeQuery();
            RoleMapper mapper = new RoleMapper();
            roles = mapper.mapResultSetToEntity(resultSet);
            LOGGER.log(Level.INFO, "Request for find all - succeed");
            return roles;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public boolean update(Role entity) throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for update role");
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(UPDATE_ROLE);
            RoleMapper mapper = new RoleMapper();
            preparedStatement = mapper.mapEntityToPreparedStatement(preparedStatement, entity);
            preparedStatement.executeUpdate();
            LOGGER.log(Level.INFO, "Request for update role - succeed");
            return true;
        } catch (SQLException e) {
            throw new DAOTechnicalException("Execute statement error. ", e);
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public int findEntityCount() throws DAOTechnicalException {
        LOGGER.log(Level.INFO, "Request for get count");
        return findEntityCount(SELECT_COUNT);
    }
}
