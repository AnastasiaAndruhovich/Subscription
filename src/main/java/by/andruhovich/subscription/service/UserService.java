package by.andruhovich.subscription.service;

import by.andruhovich.subscription.dao.ConnectionFactory;
import by.andruhovich.subscription.dao.impl.*;
import by.andruhovich.subscription.entity.*;
import by.andruhovich.subscription.exception.DAOTechnicalException;
import by.andruhovich.subscription.exception.ConnectionTechnicalException;
import by.andruhovich.subscription.exception.ServiceTechnicalException;
import by.andruhovich.subscription.coder.PasswordCoder;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class UserService extends BaseService{
    private static final Logger LOGGER = LogManager.getLogger(UserService.class);

    public boolean confirmPassword(String login, String password) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;

        try {
            connection = connectionFactory.getConnection();
            UserDAO userDAO = new UserDAO(connection);
            String dataBasePassword = userDAO.findPasswordByLogin(login);
            return PasswordCoder.checkPassword(password, dataBasePassword);
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e.getMessage());
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    public int findUserIdByLogin(String login) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;

        try {
            connection = connectionFactory.getConnection();
            UserDAO userDAO = new UserDAO(connection);
            return userDAO.findUserIdByLogin(login);
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e.getMessage());
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    public Role findRoleByUserId(int userId) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;

        try {
            connection = connectionFactory.getConnection();
            UserDAO userDAO = new UserDAO(connection);
            return userDAO.findRoleByUserId(userId);
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e.getMessage());
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    public int signUp(String lastName, String firstName, Date birthDate, String address, String city,
                          String postalIndex, String login, String password) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        int id = -1;
        Connection connection = null;

        try {
            if (findUserIdByLogin(login) == -1) {
                connection = connectionFactory.getConnection();
                connection.setAutoCommit(false);
                UserDAO userDAO = new UserDAO(connection);
                AccountDAO accountDAO = new AccountDAO(connection);
                int intPostalIndex = Integer.parseInt(postalIndex);
                password = PasswordCoder.hashPassword(password);
                Account account = accountDAO.createEmptyAccount();
                Role role = new Role(2, "user");
                User user = new User(lastName, firstName, birthDate, address, city, intPostalIndex, login, password, role, account);
                id =  userDAO.create(user);
                connection.commit();
            }
            return id;
        } catch (DAOTechnicalException | SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException e1) {
                LOGGER.log(Level.ERROR, "Error roll back transaction");
            }
            throw new ServiceTechnicalException(e);
        } catch (ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException e1) {
                    LOGGER.log(Level.ERROR, "Error set auto commit true");
                }
            }
            connectionFactory.returnConnection(connection);
        }
    }

    public boolean blockUser(String userLogin, String adminLogin) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;

        try {
            if (findUserIdByLogin(userLogin) != -1) {
                connection = connectionFactory.getConnection();
                UserDAO userDAO = new UserDAO(connection);
                User user = userDAO.findUserByLogin(userLogin);
                User admin = userDAO.findUserByLogin(adminLogin);
                Date date = Calendar.getInstance().getTime();
                Block block = new Block(user, admin, date);
                BlockDAO blockDAO = new BlockDAO(connection);
                blockDAO.create(block);
                return true;
            }
            return false;
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e.getMessage());
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    public boolean unblockUser(String login) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;

        try {
            if (findUserIdByLogin(login) != -1) {
                connection = connectionFactory.getConnection();
                UserDAO userDAO = new UserDAO(connection);
                User user = userDAO.findUserByLogin(login);
                BlockDAO blockDAO = new BlockDAO(connection);
                return blockDAO.deleteBlockByUserId(user.getUserId());
            }
            return true;
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    public boolean updateUser(String lastname, String firstname, Date birthdate, String address, String city,
                              String postalIndex, String login, String password) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;

        try {
            if (findUserIdByLogin(login) != -1) {
                connection = connectionFactory.getConnection();
                UserDAO userDAO = new UserDAO(connection);
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
            connectionFactory.returnConnection(connection);
        }
    }

    public List<User> showUsers(String pageNumber) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;
        int number = Integer.parseInt(pageNumber);
        int startIndex = (number - 1) * ENTITY_COUNT_FOR_ONE_PAGE;
        int endIndex = startIndex + ENTITY_COUNT_FOR_ONE_PAGE;

        try {
            connection = connectionFactory.getConnection();
            UserDAO userDAO = new UserDAO(connection);
            return userDAO.findAll(startIndex, endIndex);
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    public User findUserByLogin(String login) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;

        try {
            connection = connectionFactory.getConnection();
            UserDAO userDAO = new UserDAO(connection);
            return userDAO.findUserByLogin(login);
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    public User findUserByAccountNumber(String accountNumber) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;

        try {
            connection = connectionFactory.getConnection();
            UserDAO userDAO = new UserDAO(connection);
            int intAccountNumber = Integer.parseInt(accountNumber);
            return userDAO.findUserByAccountNumber(intAccountNumber);
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    public User findUserBySubscriptionId(String id) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;

        try {
            connection = connectionFactory.getConnection();
            SubscriptionDAO subscriptionDAO = new SubscriptionDAO(connection);
            int intId = Integer.parseInt(id);
            return subscriptionDAO.findUserBySubscriptionId(intId);
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    public User findUserByPaymentNumber(String paymentNumber) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;

        try {
            connection = connectionFactory.getConnection();
            PaymentDAO paymentDAO = new PaymentDAO(connection);
            int intPaymentNumber = Integer.parseInt(paymentNumber);
            Subscription subscription = paymentDAO.findSubscriptionByPaymentNumber(intPaymentNumber);
            if (subscription != null) {
                SubscriptionDAO subscriptionDAO = new SubscriptionDAO(connection);
                return subscriptionDAO.findUserBySubscriptionId(subscription.getSubscriptionId());
            }
            return null;
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    public int findUserPageCount() throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;

        try {
            connection = connectionFactory.getConnection();
            UserDAO userDAO = new UserDAO(connection);
            int count = userDAO.findEntityCount();
            return (int)Math.ceil((double)(count) / ENTITY_COUNT_FOR_ONE_PAGE);
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

}
