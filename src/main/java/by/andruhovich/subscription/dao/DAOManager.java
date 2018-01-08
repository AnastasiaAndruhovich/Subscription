package by.andruhovich.subscription.dao;

import by.andruhovich.subscription.exception.ResourceTechnicalException;
import by.andruhovich.subscription.pool.ConnectionPool;

import java.sql.Connection;

public class DAOManager {
    private ConnectionPool connectionPool = ConnectionPool.getInstance();
    private static final int WAITING_TIME = 10;

    public BaseDAO initializeDAO(BaseDAO entityDAO) throws ResourceTechnicalException {
        Connection connection = connectionPool.getConnection(WAITING_TIME);
        if (connection != null) {
            entityDAO.setConnection(connection);
            return entityDAO;
        }
        else throw new ResourceTechnicalException("Can't get connection with database!");
    }
}
