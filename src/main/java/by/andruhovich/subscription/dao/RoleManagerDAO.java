package by.andruhovich.subscription.dao;

import by.andruhovich.subscription.entity.Role;
import by.andruhovich.subscription.entity.User;
import by.andruhovich.subscription.exception.DAOTechnicalException;

import java.sql.Connection;
import java.util.List;

public abstract class RoleManagerDAO extends MediatorManagerDAO<Role> {
    public RoleManagerDAO(Connection connection) {
        super(connection);
    }

    public abstract int findIdByName(String role) throws DAOTechnicalException;

    public abstract List<User> findUsersByRoleId(int id) throws DAOTechnicalException;
}
