package by.andruhovich.subscription.service;

import by.andruhovich.subscription.dao.DAOFactory;
import by.andruhovich.subscription.dao.UserDAO;
import by.andruhovich.subscription.exception.DAOTechnicalException;
import by.andruhovich.subscription.exception.ResourceTechnicalException;
import by.andruhovich.subscription.exception.ServiceTechnicalException;

public class UserService {

    public boolean checkLogin(String login, String password) throws ServiceTechnicalException {
        //проверка на null login and password

        DAOFactory daoFactory = DAOFactory.getInstance();

        try {
            UserDAO userDAO = daoFactory.createUserDAO();
            String dataBasePassword = userDAO.findPasswordByLogin(login);
            return password.equals(dataBasePassword);
        } catch (ResourceTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e.getMessage());
        }
    }
}
