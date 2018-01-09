package by.andruhovich.subscription.dao;

import by.andruhovich.subscription.exception.ResourceTechnicalException;
import by.andruhovich.subscription.pool.ConnectionPool;

import java.sql.Connection;

public class DAOFactory {
    private static DAOFactory instance;
    private ConnectionPool connectionPool = ConnectionPool.getInstance();
    private static final int WAITING_TIME = 10;

    private DAOFactory() {
        instance = new DAOFactory();
    }

    public static DAOFactory getInstance() {
        if (instance == null) {
            instance = new DAOFactory();
        }
        return instance;
    }

    private Connection getConnection() throws ResourceTechnicalException {
        Connection connection = connectionPool.getConnection(WAITING_TIME);
        if (connection != null) {
            return connection;
        }
        else throw new ResourceTechnicalException("Can't get connection with database!");
    }

    public UserDAO createUserDAO() throws ResourceTechnicalException {
        UserDAO userDAO = new UserDAO(getConnection());
        return userDAO;
    }
}
