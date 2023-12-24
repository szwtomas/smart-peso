package com.smartpeso.repositories;

import com.smartpeso.model.UsdPrices;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Date;

public class UsdPricesRowMapper implements RowMapper<UsdPrices> {
    @Override
    public UsdPrices mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new UsdPrices(
                Date.from(rs.getTimestamp("date").toInstant()),
                roundDouble(rs.getDouble("usdOfficial")),
                roundDouble(rs.getDouble("usdMEP")),
                roundDouble(rs.getDouble("usdCCL")),
                roundDouble(rs.getDouble("usdBlue"))
        );
    }

    private double roundDouble(double number) {
        DecimalFormat df = new DecimalFormat("#.##");
        return Double.parseDouble(df.format(number));
    }
}
