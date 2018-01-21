package by.andruhovich.subscription.dao;

import by.andruhovich.subscription.dao.impl.*;
import by.andruhovich.subscription.exception.ConnectionTechnicalException;
import by.andruhovich.subscription.pool.ConnectionPool;

import java.sql.Connection;

public class DAOFactory {
    private static DAOFactory instance;
    private ConnectionPool connectionPool;

    private DAOFactory() {
        connectionPool = ConnectionPool.getInstance();
    }

    public static DAOFactory getInstance() {
        if (instance == null) {
            instance = new DAOFactory();
        }
        return instance;
    }

    private Connection getConnection() throws ConnectionTechnicalException {
        Connection connection = connectionPool.getConnection();
        if (connection != null) {
            return connection;
        }
        else {
            throw new ConnectionTechnicalException("Can't get connection with database!");
        }
    }

    public UserDAO createUserDAO() throws ConnectionTechnicalException {
        return new UserDAO(getConnection());
    }

    public AccountDAO createAccountDAO() throws ConnectionTechnicalException {
        return new AccountDAO(getConnection());
    }

    public RoleDAO createRoleDAO() throws ConnectionTechnicalException {
        return new RoleDAO(getConnection());
    }

    public GenreDAO createGenreDAO() throws ConnectionTechnicalException {
        return new GenreDAO(getConnection());
    }

    public PublicationDAO createPublicationDAO() throws ConnectionTechnicalException {
        return new PublicationDAO(getConnection());
    }

    public PublicationTypeDAO createPublicationTypeDAO() throws ConnectionTechnicalException {
        return new PublicationTypeDAO(getConnection());
    }

    public AuthorPublicationDAO createAuthorPublicationDAO() throws ConnectionTechnicalException {
        return new AuthorPublicationDAO(getConnection());
    }

    public BlockDAO createBlockDAO() throws ConnectionTechnicalException {
        return new BlockDAO(getConnection());
    }

    public PaymentDAO createPaymentDAO() throws ConnectionTechnicalException {
        return new PaymentDAO(getConnection());
    }

    public AuthorDAO createAuthorDAO() throws ConnectionTechnicalException {
        return new AuthorDAO(getConnection());
    }

    public void closeDAO(BaseDAO baseDAO) {
        if (baseDAO != null) {
            connectionPool.returnConnection(baseDAO.getConnection());
        }
    }
}
