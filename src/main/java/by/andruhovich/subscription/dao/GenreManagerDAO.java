package by.andruhovich.subscription.dao;

import by.andruhovich.subscription.entity.Genre;
import by.andruhovich.subscription.exception.DAOTechnicalException;

import java.sql.Connection;

public abstract class GenreManagerDAO extends ManagerDAO<Genre> {
    public GenreManagerDAO(Connection connection) {
        super(connection);
    }

    public abstract int findIdByEntity(Genre genre) throws DAOTechnicalException;
}
