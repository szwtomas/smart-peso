package com.smartpeso.prices.dal;

import com.smartpeso.prices.model.UsdPrices;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PricesRepository {
    private final JdbcTemplate jdbcTemplate;

    public PricesRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public UsdPrices getCurrentUsdPrices() {
        String sql = "SELECT * FROM currencyPrices ORDER BY date DESC LIMIT 1";
        return jdbcTemplate.queryForObject(sql, new UsdPricesRowMapper());
    }

    public UsdPrices getPricesFromDaysAgo(int daysBefore) {
        String sql = String.format("SELECT * " +
                "FROM currencyPrices " +
                "WHERE DATE(date) = CURDATE() - INTERVAL %d DAY " +
                "ORDER BY date DESC " +
                "LIMIT 1", daysBefore
        );

        return jdbcTemplate.queryForObject(sql, new UsdPricesRowMapper());
    }
}
