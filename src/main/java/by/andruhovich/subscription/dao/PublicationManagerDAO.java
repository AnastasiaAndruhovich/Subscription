package by.andruhovich.subscription.dao;

import by.andruhovich.subscription.entity.Publication;

import java.sql.Connection;

public abstract class PublicationManagerDAO extends BaseDAO<Publication> {
    public PublicationManagerDAO(Connection connection) {
        super(connection);
    }
}
