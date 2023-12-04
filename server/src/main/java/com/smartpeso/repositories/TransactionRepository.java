package com.smartpeso.repositories;

import com.smartpeso.model.Transaction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

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

    public List<Transaction> getTransactionsByUserId(String userId) {
        Query query = new Query(Criteria.where("user.$id").is(userId));
        return mongoTemplate.find(query, Transaction.class, TRANSACTION_COLLECTION);
    }
}
