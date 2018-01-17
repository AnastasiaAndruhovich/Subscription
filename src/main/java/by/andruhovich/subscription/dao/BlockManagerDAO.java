package by.andruhovich.subscription.dao;

import by.andruhovich.subscription.entity.Block;
import by.andruhovich.subscription.entity.User;
import by.andruhovich.subscription.exception.DAOTechnicalException;

import java.sql.Connection;
import java.util.List;

public abstract class BlockManagerDAO extends ManagerDAO<Block> {
    public BlockManagerDAO(Connection connection) {
        super(connection);
    }
    
    public abstract List<User> findUsersByAdminId(int id) throws DAOTechnicalException;
    public abstract User findAdminByUserId(int id) throws DAOTechnicalException;
}
