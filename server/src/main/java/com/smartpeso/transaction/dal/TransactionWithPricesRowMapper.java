package com.smartpeso.transaction.dal;

import com.smartpeso.prices.model.UsdPrices;
import com.smartpeso.transaction.model.Transaction;
import com.smartpeso.transaction.model.TransactionWithPrices;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class TransactionWithPricesRowMapper implements RowMapper<TransactionWithPrices> {
    @Override
    public TransactionWithPrices mapRow(ResultSet rs, int rowNum) throws SQLException {
        Transaction transaction = new Transaction(
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

        UsdPrices prices = new UsdPrices(
                rs.getDate("date"),
                rs.getDouble("usdOfficial"),
                rs.getDouble("usdMEP"),
                rs.getDouble("usdCCL"),
                rs.getDouble("usdBlue")
        );

        return new TransactionWithPrices(transaction, prices);
    }
}
