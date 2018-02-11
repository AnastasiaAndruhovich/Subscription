package by.andruhovich.subscription.service;

import by.andruhovich.subscription.coder.PasswordCoder;
import by.andruhovich.subscription.dao.AccountManagerDAO;
import by.andruhovich.subscription.dao.BlockManagerDAO;
import by.andruhovich.subscription.dao.UserManagerDAO;
import by.andruhovich.subscription.dao.impl.AccountDAO;
import by.andruhovich.subscription.dao.impl.BlockDAO;
import by.andruhovich.subscription.dao.impl.UserDAO;
import by.andruhovich.subscription.entity.Account;
import by.andruhovich.subscription.entity.Block;
import by.andruhovich.subscription.entity.Role;
import by.andruhovich.subscription.entity.User;
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

/**
 * Provides methods to process user information
 */
public class UserService extends BaseService{
    private static final Logger LOGGER = LogManager.getLogger(UserService.class);

    /**
     * @param userId User id
     * @param password Password
     * @return Result from confirm password from jsp and from database
     * @throws ServiceTechnicalException
     *          If there was an error during processing operation
     */
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

    /**
     * @param login Login
     * @return Relevant User
     * @throws ServiceTechnicalException
 *              If there was an error during processing operation
     */
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

    /**
     * @param userId User id
     * @return Relevant role from database
     * @throws ServiceTechnicalException
     *          If there was an error during processing operation
     */
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

    /**
     * @param lastName User last name
     * @param firstName User first name
     * @param birthDate User birth date
     * @param address User address
     * @param city User city
     * @param postalIndex User postal index
     * @param login User login
     * @param password User password
     * @return Created user id from database
     * @throws ServiceTechnicalException
     *          If there was an error during processing operation
     */
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

    /**
     * @param userId User id
     * @param adminId User with role admin id
     * @return {@code true} if the operation has been completed successfully
     *         {@code false} otherwise
     * @throws ServiceTechnicalException
     *          If there was an error during processing operation
     */
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

    /**
     * @param userId User id
     * @param adminId User with role admin id
     * @return {@code true} if the operation has been completed successfully
     *         {@code false} otherwise
     * @throws ServiceTechnicalException
     *          If there was an error during processing operation
     */
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

    /**
     * @param userId User id
     * @param lastName User last name
     * @param firstName User first name
     * @param birthDate User birth date
     * @param address User address
     * @param city User city
     * @param postalIndex User postal index
     * @param login User login
     * @return {@code true} if the operation has been completed successfully
     *         {@code false} otherwise
     * @throws ServiceTechnicalException
     *          If there was an error during processing operation
     */
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

    /**
     * @param pageNumber Current page number from jsp
     * @return User list from database
     * @throws ServiceTechnicalException
     *          If there was an error during processing operation
     */
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

    /**
     * @return User count from database
     * @throws ServiceTechnicalException
     *          If there was an error during processing operation
     */
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

    /**
     * @param userId User id
     * @return Relevant blocked user result
     * @throws ServiceTechnicalException
     *          If there was an error during processing operation
     */
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

    /**
     * @param userId User id
     * @return Relevant user from database
     * @throws ServiceTechnicalException
     *          If there was an error during processing operation
     */
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

    /**
     * @param userId User id
     * @param password  User password
     * @return {@code true} if the operation has been completed successfully
     *         {@code false} otherwise
     * @throws ServiceTechnicalException
     *          If there was an error during processing operation
     */
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

    /**
     * @param adminId User with role admin id
     * @param pageNumber Current page number from jsp
     * @return User list relevant to admin id
     * @throws ServiceTechnicalException
     *          If there was an error during processing operation
     */
    public List<User> findBlockedUsersByAdminId(String adminId, String pageNumber) throws ServiceTechnicalException {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection connection = null;
        int page = Integer.parseInt(pageNumber);
        int id = Integer.parseInt(adminId);

        try {
            connection = connectionFactory.getConnection();
            BlockManagerDAO blockManagerDAO = new BlockDAO(connection);
            int startIndex = (page - 1) * ENTITY_COUNT_FOR_ONE_PAGE;
            List<User> users = blockManagerDAO.findUsersByAdminId(id, startIndex, ENTITY_COUNT_FOR_ONE_PAGE);
            return FillOutEntityService.fillOutUserList(users);
        } catch (DAOTechnicalException | ConnectionTechnicalException e) {
            throw new ServiceTechnicalException(e);
        } finally {
            connectionFactory.returnConnection(connection);
        }
    }

}
