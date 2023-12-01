package com.smartpeso.services;

import com.mongodb.client.MongoDatabase;
import com.smartpeso.services.healthCheck.HealthCheckService;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoTemplate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class HealthCheckServiceTest {
    @Mock
    private MongoTemplate mongoTemplateMock;
    @Mock
    private MongoDatabase mongoDatabaseMock;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void healthCheck_mongoIsOk_shouldNotThrowException() {
        Document pingResult = new Document().append("ok", "1");
        when(mongoDatabaseMock.runCommand(any(Document.class))).thenReturn(pingResult);
        when(mongoTemplateMock.getDb()).thenReturn(mongoDatabaseMock);

        HealthCheckService unit = new HealthCheckService(mongoTemplateMock);

        unit.doHealthCheck();
    }

    @Test
    public void healthCheck_mongoPingFails_shouldThrowException() {
        Document pingResult = new Document().append("ok", "-1");
        when(mongoDatabaseMock.runCommand(any(Document.class))).thenReturn(pingResult);
        when(mongoTemplateMock.getDb()).thenReturn(mongoDatabaseMock);

        HealthCheckService unit = new HealthCheckService(mongoTemplateMock);

        assertThrows(RuntimeException.class, unit::doHealthCheck);
    }
}
