package by.andruhovich.subscription.mapper;

import by.andruhovich.subscription.entity.Role;
import by.andruhovich.subscription.exception.DAOTechnicalException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Provides methods to prepare Role entity for setting and getting from database
 */
public class RoleMapper implements EntityMapper<Role> {
    /**
     * @param resultSet java.sql.ResultSet from database to map on entity
     * @return Role list from resultSet
     * @throws DAOTechnicalException
     *          If there was an error during mapping resultSet
     */
    @Override
    public List<Role> mapResultSetToEntity(ResultSet resultSet) throws DAOTechnicalException {
        List<Role> roles = new LinkedList<>();
        Role role;

        try {
            while (resultSet.next()) {
                int roleId = resultSet.getInt("role_id");
                String name = resultSet.getString("name");
                role = new Role(roleId, name);
                roles.add(role);
            }
            return roles;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        }
    }

    /**
     * @param preparedStatement java.sql.Statement with all necessary parameters
     * @param entity Role to be set in database
     * @return Filled out statement by entity
     * @throws DAOTechnicalException
     *          If there was an error during mapping resultSet
     */
    @Override
    public PreparedStatement mapEntityToPreparedStatement(PreparedStatement preparedStatement, Role entity) throws DAOTechnicalException {
        try {
            preparedStatement.setString(1, entity.getName());
            return preparedStatement;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        }
    }
}
