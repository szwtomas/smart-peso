package com.smartpeso.services.healthCheck;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class HealthCheckService {
    JdbcTemplate jdbcTemplate;

    public HealthCheckService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void doHealthCheck() {
        jdbcTemplate.execute("SELECT 1");
    }
}
