package by.andruhovich.subscription.dao;

import by.andruhovich.subscription.entity.Genre;
import by.andruhovich.subscription.entity.Publication;
import by.andruhovich.subscription.exception.DAOTechnicalException;

import java.sql.Connection;
import java.util.List;

public abstract class GenreManagerDAO extends MediatorManagerDAO <Genre> {
    public GenreManagerDAO(Connection connection) {
        super(connection);
    }

    public abstract List<Publication> findPublicationsByGenreId(int id) throws DAOTechnicalException;
}
