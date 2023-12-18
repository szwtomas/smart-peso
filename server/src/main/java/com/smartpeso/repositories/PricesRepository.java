package com.smartpeso.repositories;

import com.smartpeso.model.UsdPrices;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PricesRepository {
    private final JdbcTemplate jdbcTemplate;

    public PricesRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public UsdPrices getLastPrices() {
        String sql = getLastPricesQuery();
        return jdbcTemplate.queryForObject(sql, new UsdPricesRowMapper());
    }

    private String getLastPricesQuery() {
        return """
                SELECT *
                FROM currencyPrices
                WHERE date = (
                    SELECT MAX(currencyPrices.date)
                    FROM currencyPrices
                );""";
    }
}
