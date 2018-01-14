package by.andruhovich.subscription.dao;

import by.andruhovich.subscription.exception.DAOTechnicalException;

import java.sql.Connection;

public abstract class MediatorManagerDAO <T> extends ManagerDAO <T>{
    public MediatorManagerDAO(Connection connection) {
        super(connection);
    }

    public abstract T findEntityById(int id) throws DAOTechnicalException;
}
