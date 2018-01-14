package by.andruhovich.subscription.dao;

import by.andruhovich.subscription.entity.Role;
import by.andruhovich.subscription.exception.DAOTechnicalException;

import java.sql.Connection;

public abstract class RoleManagerDAO extends MediatorManagerDAO<Role> {
    public RoleManagerDAO(Connection connection) {
        super(connection);
    }

    public abstract int findIdByName(String role) throws DAOTechnicalException;
}
