package com.smartpeso.controllers;

import com.smartpeso.services.healthCheck.HealthCheckService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@Slf4j
@RestController
@RequestMapping("/health-check")
public class HealthCheckController {
    private final HealthCheckService healthCheckService;

    public HealthCheckController(HealthCheckService healthCheckService) {
        this.healthCheckService = healthCheckService;
    }

    @GetMapping
    public ResponseEntity<String> healthCheck() {
        try {
            healthCheckService.doHealthCheck();
            return new ResponseEntity<>("0", HttpStatus.OK);
        } catch(Exception e) {
            log.error("Health check failed with error: " + e.getMessage());
            return new ResponseEntity<>("Health Check failed", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
