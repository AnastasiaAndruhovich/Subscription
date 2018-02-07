package by.andruhovich.subscription.dao;

import by.andruhovich.subscription.entity.Role;

import java.sql.Connection;

public abstract class RoleManagerDAO extends ManagerDAO<Role> {
    public RoleManagerDAO(Connection connection) {
        super(connection);
    }

}
