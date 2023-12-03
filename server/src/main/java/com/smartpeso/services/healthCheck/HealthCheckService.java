package com.smartpeso.services.healthCheck;

import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class HealthCheckService {
    private final String MONGO_PING_OK = "1.0";
    MongoTemplate mongoTemplate;

    public HealthCheckService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public void doHealthCheck() {
        verifyMongoIsUp();
    }

    private void verifyMongoIsUp() {
        Document ping = new Document().append("ping", "1");
        String pingResult = mongoTemplate.getDb().runCommand(ping).get("ok").toString();
        if (!pingResult.equals(MONGO_PING_OK)) {
            throw new RuntimeException("Mongo health check failed");
        }
    }
}
