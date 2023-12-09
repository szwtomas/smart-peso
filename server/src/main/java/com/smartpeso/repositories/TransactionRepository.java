package com.smartpeso.repositories;

import com.mongodb.client.result.DeleteResult;
import com.smartpeso.model.Transaction;
import com.smartpeso.repositories.exceptions.DeleteTransactionException;
import com.smartpeso.repositories.exceptions.TransactionCreationException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Optional;

@Repository
public class TransactionRepository {
    private final String TRANSACTION_COLLECTION = "transactions";
    private final MongoTemplate mongoTemplate;

    public TransactionRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public Transaction upsertTransaction(Transaction transaction) {
        try {
            return mongoTemplate.save(transaction, TRANSACTION_COLLECTION);
        } catch(Exception e) {
            throw new TransactionCreationException("Failed creating transaction");
        }
    }

    public Optional<Transaction> getTransactionById(String transactionId) {
        Query query = new Query(Criteria.where("_id").is(transactionId));
        Transaction transaction = mongoTemplate.findOne(query, Transaction.class, TRANSACTION_COLLECTION);
        return Optional.ofNullable(transaction);
    }

    public List<Transaction> getTransactionsByUserId(String userId) {
        Query query = new Query(Criteria.where("userId").is(userId));
        return mongoTemplate.find(query, Transaction.class, TRANSACTION_COLLECTION);
    }

    public void deleteTransaction(Transaction transaction) {
        DeleteResult deleteResult = mongoTemplate.remove(transaction, TRANSACTION_COLLECTION);
        if (deleteResult.getDeletedCount() != 1) {
            throw new DeleteTransactionException("transaction with transactionId " + transaction.getTransactionId() + " not found");
        }
    }
}
