package com.smartpeso.repositories;

import com.smartpeso.model.Transaction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class TransactionRepository {
    private final String TRANSACTION_COLLECTION = "transactions";
    private final MongoTemplate mongoTemplate;

    public TransactionRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public Transaction createTransaction(Transaction transaction) {
        try {
            return mongoTemplate.save(transaction, TRANSACTION_COLLECTION);
        } catch(Exception e) {
            throw new TransactionCreationException("Failed creating transaction");
        }
    }
}
