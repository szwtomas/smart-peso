package com.smartpeso.repositories;

import com.smartpeso.model.UsdPrices;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class UsdPricesRowMapper implements RowMapper<UsdPrices> {

    @Override
    public UsdPrices mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new UsdPrices(
                Date.from(rs.getTimestamp("date").toInstant()),
                rs.getDouble("usdOfficial"),
                rs.getDouble("usdMEP"),
                rs.getDouble("usdCCL"),
                rs.getDouble("usdBlue")
        );
    }
}
