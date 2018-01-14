package by.andruhovich.subscription.mapper;

import by.andruhovich.subscription.exception.DAOTechnicalException;
import by.andruhovich.subscription.mapper.EntityMapper;
import by.andruhovich.subscription.entity.Role;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class RoleMapper implements EntityMapper<Role> {
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
