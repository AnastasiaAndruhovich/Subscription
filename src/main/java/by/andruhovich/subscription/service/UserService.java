package by.andruhovich.subscription.service;

import by.andruhovich.subscription.coder.PasswordCoder;
import by.andruhovich.subscription.dao.*;
import by.andruhovich.subscription.dao.impl.*;
import by.andruhovich.subscription.entity.*;
import by.andruhovich.subscription.exception.ConnectionTechnicalException;
import by.andruhovich.subscription.exception.DAOTechnicalException;
import by.andruhovich.subscription.exception.ServiceTechnicalException;
import by.andruhovich.subscription.pool.ConnectionFactory;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class UserService extends BaseService{
    private static final Logger LOGGER = LogManager.getLogger(UserService.class);

    public boolean confirmPassword(String userId, String password) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;

        int id = Integer.parseInt(userId);

        try {
            connection = connectionFactory.getConnection();
            UserManagerDAO userManagerDAO = new UserDAO(connection);
            String dataBasePassword = userManagerDAO.findPasswordById(id);
            return dataBasePassword != null && PasswordCoder.checkPassword(password, dataBasePassword);
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
            UserManagerDAO userManagerDAO = new UserDAO(connection);
            return userManagerDAO.findUserIdByLogin(login);
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
            UserManagerDAO userManagerDAO = new UserDAO(connection);
            return userManagerDAO.findRoleByUserId(userId);
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
                UserManagerDAO userManagerDAO = new UserDAO(connection);
                AccountManagerDAO accountManagerDAO = new AccountDAO(connection);
                int intPostalIndex = Integer.parseInt(postalIndex);
                password = PasswordCoder.hashPassword(password);
                Account account = accountManagerDAO.createEmptyAccount();
                Role role = new Role(2, "user");
                User user = new User(lastName, firstName, birthDate, address, city, intPostalIndex, login, password, role, account);
                id =  userManagerDAO.create(user);
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

    public boolean blockUser(String userId, String adminId) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;

        int intUserId = Integer.parseInt(userId);
        int intAdminId = Integer.parseInt(adminId);
        if (intAdminId == intUserId) return false;

        try {
            connection = connectionFactory.getConnection();
            UserManagerDAO userManagerDAO = new UserDAO(connection);
            User user = userManagerDAO.findEntityById(intUserId);
            User admin = userManagerDAO.findEntityById(intAdminId);
            Date date = Calendar.getInstance().getTime();
            Block block = new Block(user, admin, date);
            BlockDAO blockDAO = new BlockDAO(connection);
            blockDAO.create(block);
            return true;
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e.getMessage());
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    public boolean unblockUser(String userId, String adminId) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;

        int intUserId = Integer.parseInt(userId);
        int intAdminId = Integer.parseInt(adminId);

        try {
            connection = connectionFactory.getConnection();
            UserManagerDAO userManagerDAO = new UserDAO(connection);
            BlockManagerDAO blockManagerDAO = new BlockDAO(connection);
            User user = userManagerDAO.findEntityById(intUserId);
            User admin = blockManagerDAO.findAdminByUserId(intUserId);
            return intAdminId == admin.getUserId() && blockManagerDAO.deleteBlockByUserId(user.getUserId());
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    public boolean updateUser(String userId, String lastName, String firstName, Date birthDate, String address, String city,
                              String postalIndex, String login) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;

        try {
            if (findUserIdByLogin(login) != -1) {
                connection = connectionFactory.getConnection();
                UserManagerDAO userManagerDAO = new UserDAO(connection);
                int id = Integer.parseInt(userId);
                Account account = userManagerDAO.findAccountByUserId(id);
                int intPostalIndex = Integer.parseInt(postalIndex);
                String password = userManagerDAO.findPasswordById(id);
                Role role = userManagerDAO.findRoleByUserId(id);
                User user = new User(id, lastName, firstName, birthDate, address, city, intPostalIndex, login, password, role, account);
                return userManagerDAO.update(user);
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
        int page = Integer.parseInt(pageNumber);

        try {
            connection = connectionFactory.getConnection();
            UserManagerDAO userManagerDAO = new UserDAO(connection);
            int startIndex = (page - 1) * ENTITY_COUNT_FOR_ONE_PAGE;
            List<User> users = userManagerDAO.findAll(startIndex, ENTITY_COUNT_FOR_ONE_PAGE);
            return FillOutEntityService.fillOutUserList(users);
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
            UserManagerDAO userManagerDAO = new UserDAO(connection);
            int intAccountNumber = Integer.parseInt(accountNumber);
            return userManagerDAO.findUserByAccountNumber(intAccountNumber);
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
            SubscriptionManagerDAO subscriptionManagerDAO = new SubscriptionDAO(connection);
            int intId = Integer.parseInt(id);
            return subscriptionManagerDAO.findUserBySubscriptionId(intId);
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
            PaymentManagerDAO paymentManagerDAO = new PaymentDAO(connection);
            int intPaymentNumber = Integer.parseInt(paymentNumber);
            Subscription subscription = paymentManagerDAO.findSubscriptionByPaymentNumber(intPaymentNumber);
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

    public boolean isUserBlocked(String userId) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;

        int intUserId = Integer.parseInt(userId);

        try {
            connection = connectionFactory.getConnection();
            BlockManagerDAO blockManagerDAO = new BlockDAO(connection);
            User admin = blockManagerDAO.findAdminByUserId(intUserId);
            return admin != null;
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e.getMessage());
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    public User findUserById(String userId) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;

        int id = Integer.parseInt(userId);

        try {
            connection = connectionFactory.getConnection();
            UserManagerDAO userManagerDAO = new UserDAO(connection);
            return userManagerDAO.findEntityById(id);
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e.getMessage());
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

    public boolean changePassword(String userId, String password) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;

        password = PasswordCoder.hashPassword(password);

        try {
            connection = connectionFactory.getConnection();
            UserManagerDAO userManagerDAO = new UserDAO(connection);
            int id = Integer.parseInt(userId);
            User user = userManagerDAO.findEntityById(id);
            if (user != null) {
                LinkedList<User> users = new LinkedList<>();
                users.add(user);
                user = FillOutEntityService.fillOutUserList(users).get(0);
                user.setPassword(password);
                return userManagerDAO.update(user);
            }
            return false;
        } catch (ConnectionTechnicalException | DAOTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

}
