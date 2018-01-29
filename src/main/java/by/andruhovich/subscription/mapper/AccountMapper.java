package by.andruhovich.subscription.mapper;

import by.andruhovich.subscription.exception.DAOTechnicalException;
import by.andruhovich.subscription.mapper.EntityMapper;
import by.andruhovich.subscription.entity.Account;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class AccountMapper implements EntityMapper<Account> {
    @Override
    public List<Account> mapResultSetToEntity(ResultSet resultSet) throws DAOTechnicalException {
        List<Account> accounts = new LinkedList<>();
        Account account;
        try {
            while (resultSet.next()) {
                int accountNumber = resultSet.getInt("account_number");
                BigDecimal balance = resultSet.getBigDecimal("balance");
                BigDecimal loan = resultSet.getBigDecimal("loan");
                account = new Account(accountNumber, balance, loan);
                accounts.add(account);
            }
            return accounts;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        }
    }

    @Override
    public PreparedStatement mapEntityToPreparedStatement(PreparedStatement preparedStatement, Account entity) throws DAOTechnicalException {
        try {
            preparedStatement.setBigDecimal(1, entity.getBalance());
            preparedStatement.setBigDecimal(2, entity.getLoan());
            return preparedStatement;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        }
    }
}
