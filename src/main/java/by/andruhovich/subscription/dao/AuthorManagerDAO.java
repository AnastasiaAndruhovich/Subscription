package by.andruhovich.subscription.dao;

import by.andruhovich.subscription.entity.Author;

import java.sql.Connection;

public abstract class AuthorManagerDAO extends BaseDAO<Author>{
    public AuthorManagerDAO(Connection connection) {
        super(connection);
    }
}
