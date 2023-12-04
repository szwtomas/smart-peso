package com.smartpeso.services.transaction;

import com.smartpeso.model.Transaction;
import com.smartpeso.model.User;
import com.smartpeso.model.dto.transaction.TransactionDTO;
import com.smartpeso.repositories.TransactionRepository;
import com.smartpeso.validators.TransactionValidator;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionValidator transactionValidator;

    public TransactionService(TransactionRepository transactionRepository, TransactionValidator transactionValidator) {
        this.transactionRepository = transactionRepository;
        this.transactionValidator = transactionValidator;
    }

    public Transaction createTransaction(TransactionDTO transactionDTO, User user) {
        Transaction transaction = createTransactionModel(transactionDTO, user);
        transactionValidator.validateTransaction(transaction);
        return transactionRepository.createTransaction(transaction);
    }

    private String createTransactionId() {
        return UUID.randomUUID().toString();
    }

    private Transaction createTransactionModel(TransactionDTO createTransactionRequest, User user) {
        return new Transaction(
                createTransactionId(),
                user,
                createTransactionRequest.name(),
                new Date(),
                createTransactionRequest.type(),
                createTransactionRequest.currency(),
                createTransactionRequest.value(),
                createTransactionRequest.category(),
                createTransactionRequest.description()
        );
    }
}
