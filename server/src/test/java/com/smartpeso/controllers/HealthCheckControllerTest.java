package com.smartpeso.controllers;

import com.smartpeso.services.healthCheck.HealthCheckService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;


public class HealthCheckControllerTest {
    @Mock
    private HealthCheckService healthCheckServiceMock;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void givenHealthCheckIsOk_healthCheckShouldReturn0AsStringAndOkStatusCode() {
        HealthCheckController unit = new HealthCheckController(healthCheckServiceMock);
        ResponseEntity<String> response = unit.healthCheck();

        assertEquals(200, response.getStatusCode().value());
        assertEquals("0", response.getBody());
    }

    @Test
    public void givenHealthCheckFails_shouldReturn500AsResponseStatusCode() {
        doThrow(new RuntimeException("some error")).when(healthCheckServiceMock).doHealthCheck();
        HealthCheckController unit = new HealthCheckController(healthCheckServiceMock);

        ResponseEntity<String> response = unit.healthCheck();

        assertEquals(500, response.getStatusCode().value());
    }
}
