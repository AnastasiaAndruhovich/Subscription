package by.andruhovich.subscription.dao;

import by.andruhovich.subscription.dao.ManagerDAO;
import by.andruhovich.subscription.entity.Author;
import by.andruhovich.subscription.exception.DAOTechnicalException;

import java.sql.Connection;

public abstract class AuthorManagerDAO extends ManagerDAO<Author> {
    public AuthorManagerDAO(Connection connection) {
        super(connection);
    }

    public abstract int findIdByEntity(Author author) throws DAOTechnicalException;
}
