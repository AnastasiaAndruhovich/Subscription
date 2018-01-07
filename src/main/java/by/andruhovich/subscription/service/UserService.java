package by.andruhovich.subscription.service;

import by.andruhovich.subscription.dao.DAOManager;
import by.andruhovich.subscription.dao.UserDAO;
import by.andruhovich.subscription.exception.ResourceTechnicalException;
import by.andruhovich.subscription.exception.ServiceTechnicalException;

public class UserService {

    public boolean checkLogin(String login, String password) throws ServiceTechnicalException {
        //проверка на null login and password

        DAOManager daoManager = new DAOManager();
        UserDAO userDAO = new UserDAO();
        try {
            userDAO = (UserDAO) daoManager.initializeDAO(userDAO);
            String dataBasePassword = userDAO.findPasswordByLogin(login);
            return password.equals(dataBasePassword);
        } catch (ResourceTechnicalException e) {
            //TODO log
            throw new ServiceTechnicalException(e.getMessage());
        }
    }
}
