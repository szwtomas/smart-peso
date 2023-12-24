package com.smartpeso.transaction.dal;

import com.smartpeso.transaction.model.Transaction;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class TransactionRowMapper implements RowMapper<Transaction> {

    @Override
    public Transaction mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Transaction(
                rs.getInt("transactionId"),
                rs.getInt("userId"),
                rs.getString("name"),
                rs.getObject("date", LocalDateTime.class),
                rs.getString("type"),
                rs.getString("currency"),
                rs.getDouble("value"),
                rs.getString("category"),
                rs.getString("description"),
                rs.getString("paymentMethod")
        );
    }
}
