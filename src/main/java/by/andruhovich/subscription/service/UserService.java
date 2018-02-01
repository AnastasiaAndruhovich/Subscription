package by.andruhovich.subscription.service;

import by.andruhovich.subscription.dao.DAOFactory;
import by.andruhovich.subscription.dao.impl.AccountDAO;
import by.andruhovich.subscription.dao.impl.BlockDAO;
import by.andruhovich.subscription.dao.impl.PaymentDAO;
import by.andruhovich.subscription.dao.impl.UserDAO;
import by.andruhovich.subscription.entity.*;
import by.andruhovich.subscription.exception.DAOTechnicalException;
import by.andruhovich.subscription.exception.ConnectionTechnicalException;
import by.andruhovich.subscription.exception.ServiceTechnicalException;
import by.andruhovich.subscription.coder.PasswordCoder;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class UserService extends BaseService{
    public boolean confirmPassword(String login, String password) throws ServiceTechnicalException {
        DAOFactory daoFactory = DAOFactory.getInstance();
        UserDAO userDAO = null;

        try {
            userDAO = daoFactory.createUserDAO();
            String dataBasePassword = userDAO.findPasswordByLogin(login);
            return PasswordCoder.checkPassword(password, dataBasePassword);
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e.getMessage());
        } finally {
            daoFactory.closeDAO(userDAO);
        }
    }

    public int findUserIdByLogin(String login) throws ServiceTechnicalException {
        DAOFactory daoFactory = DAOFactory.getInstance();
        UserDAO userDAO = null;

        try {
            userDAO = daoFactory.createUserDAO();
            return userDAO.findUserIdByLogin(login);
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e.getMessage());
        } finally {
            daoFactory.closeDAO(userDAO);
        }
    }

    public Role findRoleByUserId(int userId) throws ServiceTechnicalException {
        DAOFactory daoFactory = DAOFactory.getInstance();
        UserDAO userDAO = null;

        try {
            userDAO = daoFactory.createUserDAO();
            return userDAO.findRoleByUserId(userId);
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e.getMessage());
        } finally {
            daoFactory.closeDAO(userDAO);
        }
    }

    public int signUp(String lastname, String firstname, Date birthdate, String address, String city,
                          String postalIndex, String login, String password) throws ServiceTechnicalException {
        DAOFactory daoFactory = DAOFactory.getInstance();
        UserDAO userDAO = null;
        AccountDAO accountDAO = null;

        try {
            if (findUserIdByLogin(login) == -1) {
                userDAO = daoFactory.createUserDAO();
                accountDAO = daoFactory.createAccountDAO();
                int intPostalIndex = Integer.parseInt(postalIndex);
                password = PasswordCoder.hashPassword(password);
                Account account = accountDAO.createEmptyAccount();
                Role role = new Role(2, "user");
                User user = new User(lastname, firstname, birthdate, address, city, intPostalIndex, login, password, role, account);
                return userDAO.create(user);
            }
            return -1;
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e.getMessage());
        } finally {
            daoFactory.closeDAO(userDAO);
            daoFactory.closeDAO(accountDAO);
        }
    }

    public boolean blockUser(String userLogin, String adminLogin) throws ServiceTechnicalException {
        DAOFactory daoFactory = DAOFactory.getInstance();
        UserDAO userDAO = null;
        BlockDAO blockDAO = null;

        try {
            if (findUserIdByLogin(userLogin) != -1) {
                userDAO = daoFactory.createUserDAO();
                User user = userDAO.findUserByLogin(userLogin);
                User admin = userDAO.findUserByLogin(adminLogin);
                Date date = Calendar.getInstance().getTime();
                Block block = new Block(user, admin, date);
                blockDAO = daoFactory.createBlockDAO();
                blockDAO.create(block);
                return true;
            }
            return false;
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e.getMessage());
        } finally {
            daoFactory.closeDAO(userDAO);
            daoFactory.closeDAO(blockDAO);
        }
    }

    public boolean unblockUser(String login) throws ServiceTechnicalException {
        DAOFactory daoFactory = DAOFactory.getInstance();
        UserDAO userDAO = null;
        BlockDAO blockDAO = null;

        try {
            if (findUserIdByLogin(login) != -1) {
                userDAO = daoFactory.createUserDAO();
                User user = userDAO.findUserByLogin(login);
                blockDAO = daoFactory.createBlockDAO();
                return blockDAO.deleteBlockByUserId(user.getUserId());
            }
            return true;
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            daoFactory.closeDAO(userDAO);
            daoFactory.closeDAO(blockDAO);
        }
    }

    public boolean updateUser(String lastname, String firstname, Date birthdate, String address, String city,
                              String postalIndex, String login, String password) throws ServiceTechnicalException {
        DAOFactory daoFactory = DAOFactory.getInstance();
        UserDAO userDAO = null;

        try {
            if (findUserIdByLogin(login) != -1) {
                userDAO = daoFactory.createUserDAO();
                int userId = userDAO.findUserByLogin(login).getUserId();
                Account account = userDAO.findAccountByUserId(userId);
                int intPostalIndex = Integer.parseInt(postalIndex);
                password = PasswordCoder.hashPassword(password);
                User user = new User(lastname, firstname, birthdate, address, city, intPostalIndex, login, password, account);
                return userDAO.update(user);
            }
            return false;
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            daoFactory.closeDAO(userDAO);
        }
    }

    public List<User> showUsers(String pageNumber) throws ServiceTechnicalException {
        DAOFactory daoFactory = DAOFactory.getInstance();
        UserDAO userDAO = null;
        int number = Integer.parseInt(pageNumber);
        int startIndex = (number - 1) * ENTITY_COUNT_FOR_ONE_PAGE;
        int endIndex = startIndex + ENTITY_COUNT_FOR_ONE_PAGE;

        try {
            userDAO = daoFactory.createUserDAO();
            return userDAO.findAll(startIndex, endIndex);
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            daoFactory.closeDAO(userDAO);
        }
    }

    public User findUserByLogin(String login) throws ServiceTechnicalException {
        DAOFactory daoFactory = DAOFactory.getInstance();
        UserDAO userDAO = null;

        try {
            userDAO = daoFactory.createUserDAO();
            return userDAO.findUserByLogin(login);
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            daoFactory.closeDAO(userDAO);
        }
    }

    public User findUserByAccountNumber(String accountNumber) throws ServiceTechnicalException {
        DAOFactory daoFactory = DAOFactory.getInstance();
        UserDAO userDAO = null;

        try {
            userDAO = daoFactory.createUserDAO();
            int intAccountNumber = Integer.parseInt(accountNumber);
            return userDAO.findUserByAccountNumber(intAccountNumber);
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            daoFactory.closeDAO(userDAO);
        }
    }

    public User findUserBySubscriptionId(String id) throws ServiceTechnicalException {
        DAOFactory daoFactory = DAOFactory.getInstance();
        UserDAO userDAO = null;

        try {
            userDAO = daoFactory.createUserDAO();
            int intId = Integer.parseInt(id);
            return userDAO.findUserBySubscriptionId(intId);
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            daoFactory.closeDAO(userDAO);
        }
    }

    public User findUserByPaymentNumber(String paymentNumber) throws ServiceTechnicalException {
        DAOFactory daoFactory = DAOFactory.getInstance();
        PaymentDAO paymentDAO = null;
        UserDAO userDAO = null;

        try {
            paymentDAO = daoFactory.createPaymentDAO();
            int intPaymentNumber = Integer.parseInt(paymentNumber);
            Subscription subscription = paymentDAO.findSubscriptionByPaymentNumber(intPaymentNumber);
            if (subscription != null) {
                userDAO = daoFactory.createUserDAO();
                return userDAO.findUserBySubscriptionId(subscription.getSubscriptionId());
            }
            return null;
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            daoFactory.closeDAO(paymentDAO);
            daoFactory.closeDAO(userDAO);
        }
    }

    public int findUserPageCount() throws ServiceTechnicalException {
        DAOFactory daoFactory = DAOFactory.getInstance();
        UserDAO userDAO = null;

        try {
            userDAO = daoFactory.createUserDAO();
            int count = userDAO.findEntityCount();
            return (int)Math.ceil((double)(count) / ENTITY_COUNT_FOR_ONE_PAGE);
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            daoFactory.closeDAO(userDAO);
        }
    }

}
