package by.andruhovich.subscription.dao;

import by.andruhovich.subscription.entity.Author;
import by.andruhovich.subscription.entity.Publication;
import by.andruhovich.subscription.exception.DAOTechnicalException;

import java.sql.Connection;
import java.util.List;

public abstract class AuthorManagerDAO extends MediatorManagerDAO<Author> {
    public AuthorManagerDAO(Connection connection) {
        super(connection);
    }

    public abstract int findIdByEntity(Author author) throws DAOTechnicalException;
}
