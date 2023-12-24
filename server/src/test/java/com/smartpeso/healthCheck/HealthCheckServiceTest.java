package com.smartpeso.healthCheck;

import com.smartpeso.healthCheck.HealthCheckService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class HealthCheckServiceTest {
    @Mock
    private JdbcTemplate jdbcTemplateMock;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void healthCheck_givenMySQLIsOk_shouldNotThrowException() {
        doNothing().when(jdbcTemplateMock).execute(anyString());
        HealthCheckService unit = new HealthCheckService(jdbcTemplateMock);
        unit.doHealthCheck();
    }

    @Test
    public void healthCheck_givenMySQLFails_shouldThrowException() {
        doThrow(new RuntimeException("some error")).when(jdbcTemplateMock).execute(anyString());
        HealthCheckService unit = new HealthCheckService(jdbcTemplateMock);
        assertThrows(Exception.class, () -> unit.doHealthCheck());
    }
}
