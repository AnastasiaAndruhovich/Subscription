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

    public abstract boolean deleteBlockByUserId(int id) throws DAOTechnicalException;

    public abstract int findBlockedUsersCountByAdminId(int adminId) throws DAOTechnicalException;

    public abstract List<User> findUsersByAdminId(int id, int startIndex, int endIndex) throws DAOTechnicalException;
    public abstract User findAdminByUserId(int id) throws DAOTechnicalException;
}
