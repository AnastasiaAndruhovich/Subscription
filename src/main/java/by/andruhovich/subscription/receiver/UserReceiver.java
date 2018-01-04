package by.andruhovich.subscription.receiver;

import by.andruhovich.subscription.dao.BaseDAO;
import by.andruhovich.subscription.dao.UserDAO;
import by.andruhovich.subscription.entity.User;
import by.andruhovich.subscription.pool.ConnectionPool;

import java.sql.Connection;

public class UserReceiver {
    private ConnectionPool connectionPool = ConnectionPool.getInstance();
    private static final int WAITING_TIME = 10;

    public boolean checkLogin(String login, String password) {
        //проверка на null login and password
        Connection connection = connectionPool.getConnection(WAITING_TIME);
        if (connection != null) {
            UserDAO userDAO = new UserDAO(connection);
            String dataBasePassword = userDAO.findPasswordByLogin(login);
            return password.equals(dataBasePassword);
        }
        return false;
    }
}
