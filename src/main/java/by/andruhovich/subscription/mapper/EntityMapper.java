package by.andruhovich.subscription.mapper;

import by.andruhovich.subscription.entity.Entity;
import by.andruhovich.subscription.exception.DAOTechnicalException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

/**
 * @param <T> Provides methods to prepare entities for setting and getting from database
 */
public interface EntityMapper<T extends Entity>{
    /**
     * @param set java.sql.ResultSet from database to map on entity
     * @return Entity list from resultSet
     * @throws DAOTechnicalException
 *              If there was an error during mapping resultSet
     */
    List<T> mapResultSetToEntity(ResultSet set) throws DAOTechnicalException;

    /**
     * @param preparedStatement java.sql.Statement with all necessary parameters
     * @param entity Entity to be set in database
     * @return Filled out statement by entity
     * @throws DAOTechnicalException
     *          If there was an error during mapping resultSet
     */
    PreparedStatement mapEntityToPreparedStatement(PreparedStatement preparedStatement, T entity) throws DAOTechnicalException;
}
