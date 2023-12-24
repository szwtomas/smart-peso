package com.smartpeso.prices.dal;

import com.smartpeso.prices.model.MonthlyPriceEntry;
import com.smartpeso.prices.model.UsdPrices;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    public List<MonthlyPriceEntry> getMonthlyUSDPrices() {
        String sql = getMonthlyAverageQuery();
        return jdbcTemplate.query(sql, new MonthlyPriceEntryRowMapper());
    }

    private String getMonthlyAverageQuery() {
        return """
            SELECT
                YEAR(date) as year,
                MONTH(date) as month,
                ROUND(AVG(usdOfficial), 2) AS usdOfficial,
                ROUND(AVG(usdMEP), 2) AS usdMEP,
                ROUND(AVG(usdBlue), 2) AS usdBlue,
                ROUND(AVG(usdCCL), 2) AS usdCCL
            FROM
                currencyPrices
            WHERE
                date >= DATE_SUB(NOW(), INTERVAL 12 MONTH)
            GROUP BY
                YEAR(date),
                MONTH(date)
            ORDER BY
                YEAR(date), MONTH(date);
        """;
    }
}
