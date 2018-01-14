package by.andruhovich.subscription.mapper;

import by.andruhovich.subscription.exception.DAOTechnicalException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public interface EntityMapper<T>{
    List<T> mapResultSetToEntity(ResultSet set) throws DAOTechnicalException;

    PreparedStatement mapEntityToPreparedStatement(PreparedStatement preparedStatement, T entity) throws DAOTechnicalException;
}
