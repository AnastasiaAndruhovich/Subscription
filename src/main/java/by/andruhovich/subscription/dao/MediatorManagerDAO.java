package by.andruhovich.subscription.dao;

import by.andruhovich.subscription.entity.Entity;
import by.andruhovich.subscription.exception.DAOTechnicalException;

import java.sql.Connection;

public abstract class MediatorManagerDAO <T extends Entity> extends ManagerDAO <T> {
    protected static final String SELECT_LAST_INSERT_ID = "SELECT LAST_INSERT_ID()";
    
    public MediatorManagerDAO(Connection connection) {
        super(connection);
    }

    public abstract T findEntityById(int id) throws DAOTechnicalException;
}
