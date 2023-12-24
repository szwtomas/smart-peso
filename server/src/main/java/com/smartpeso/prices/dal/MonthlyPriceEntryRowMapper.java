package com.smartpeso.prices.dal;

import com.smartpeso.prices.model.MonthlyPriceEntry;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MonthlyPriceEntryRowMapper implements RowMapper<MonthlyPriceEntry> {
    @Override
    public MonthlyPriceEntry mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new MonthlyPriceEntry(
                rs.getInt("year"),
                rs.getInt("month"),
                rs.getDouble("usdOfficial"),
                rs.getDouble("usdMEP"),
                rs.getDouble("usdCCL"),
                rs.getDouble("usdBlue")
        );
    }
}
