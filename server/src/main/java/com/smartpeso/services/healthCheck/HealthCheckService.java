package com.smartpeso.services.healthCheck;

import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class HealthCheckService {
    MongoTemplate mongoTemplate;

    public HealthCheckService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public void doHealthCheck() {
        verifyMongoIsUp();
    }

    private void verifyMongoIsUp() {
        Document ping = new Document().append("ping", "1");
        String pingResult = mongoTemplate.getDb().runCommand(ping).getString("ok");
        if (!pingResult.equals("1")) {
            throw new RuntimeException("Mongo health check failed");
        }
    }
}
