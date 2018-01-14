package by.andruhovich.subscription.mapper;

import by.andruhovich.subscription.exception.DAOTechnicalException;
import by.andruhovich.subscription.mapper.EntityMapper;
import by.andruhovich.subscription.entity.Block;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class BlockMapper implements EntityMapper<Block> {
    @Override
    public List<Block> mapResultSetToEntity(ResultSet set) {
        return null;
    }

    @Override
    public PreparedStatement mapEntityToPreparedStatement(PreparedStatement preparedStatement, Block entity) throws DAOTechnicalException {
        Date date = new Date(entity.getDate().getTime());

        try {
            preparedStatement.setInt(1, entity.getUser().getUserId());
            preparedStatement.setInt(2, entity.getAdmin().getUserId());
            preparedStatement.setDate(3, date);
            return preparedStatement;
        } catch (SQLException e) {
            throw new DAOTechnicalException(e.getMessage());
        }
    }
}
