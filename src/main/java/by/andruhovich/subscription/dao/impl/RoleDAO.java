package by.andruhovich.subscription.dao.impl;

import by.andruhovich.subscription.dao.RoleManagerDAO;
import by.andruhovich.subscription.entity.Role;
import by.andruhovich.subscription.exception.DAOTechnicalException;
import by.andruhovich.subscription.mapper.RoleMapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class RoleDAO extends RoleManagerDAO {
    private static final String SELECT_ID_BY_NAME = "SELECT role_id FROM roles WHERE name = ?";
    private static final String INSERT_ROLE= "INSERT INTO roles(name) VALUE (?)";
    private static final String SELECT_LAST_INSERT_ID = "SELECT LAST_INSERT_ID()";
    private static final String DELETE_ROLE_BY_ID = "DELETE FROM roles WHERE role_id = ?";
    private static final String SELECT_ROLE_BY_ID = "SELECT * FROM roles WHERE role_id = ?";
    private static final String SELECT_ALL_ROLES = "SELECT * FROM roles";
    private static final String UPDATE_ROLE = "UPDATE roles SET name = ? WHERE role_id = ?";

    public RoleDAO(Connection connection) {
        super(connection);
    }

    @Override
    public int findIdByName(String name) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;
        int id = 0;

        try {
            preparedStatement = connection.prepareStatement(SELECT_ID_BY_NAME);
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                id = resultSet.getInt("role_id");
            }
            return id;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public int create(Role entity) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;
        RoleMapper mapper = new RoleMapper();
        int id = 0;

        try {
            preparedStatement = connection.prepareStatement(INSERT_ROLE);
            preparedStatement = mapper.mapEntityToPreparedStatement(preparedStatement, entity);
            preparedStatement.executeQuery();
            preparedStatement = connection.prepareStatement(SELECT_LAST_INSERT_ID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                id = resultSet.getInt("role_id");
            }
            return id;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public boolean delete(Role entity) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(DELETE_ROLE_BY_ID);
            preparedStatement.setInt(1, entity.getRoleId());
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public Role findEntityById(int id) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;
        List<Role> roles;

        try {
            preparedStatement = connection.prepareStatement(SELECT_ROLE_BY_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            RoleMapper mapper = new RoleMapper();
            roles = mapper.mapResultSetToEntity(resultSet);
            return roles.get(0);
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public List<Role> findAll() throws DAOTechnicalException {
        List<Role> roles;
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(SELECT_ALL_ROLES);
            ResultSet resultSet = preparedStatement.executeQuery();
            RoleMapper mapper = new RoleMapper();
            roles = mapper.mapResultSetToEntity(resultSet);
            return roles;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        } finally {
            close(preparedStatement);
        }
    }

    @Override
    public boolean update(Role entity) throws DAOTechnicalException {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement(UPDATE_ROLE);
            RoleMapper mapper = new RoleMapper();
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
