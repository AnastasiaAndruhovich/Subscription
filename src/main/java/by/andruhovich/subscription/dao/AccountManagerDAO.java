package by.andruhovich.subscription.dao;

import by.andruhovich.subscription.entity.Account;

import java.sql.Connection;

public abstract class AccountManagerDAO extends BaseDAO<Account>{
    public AccountManagerDAO(Connection connection) {
        super(connection);
    }

}
