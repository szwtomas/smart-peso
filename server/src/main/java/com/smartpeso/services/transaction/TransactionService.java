package com.smartpeso.services.transaction;

import com.smartpeso.model.Transaction;
import com.smartpeso.model.User;
import com.smartpeso.model.dto.transaction.EditTransactionRequest;
import com.smartpeso.model.dto.transaction.TransactionDTO;
import com.smartpeso.repositories.TransactionRepository;
import com.smartpeso.validators.TransactionValidator;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
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
        return transactionRepository.upsertTransaction(transaction);
    }

    public List<Transaction> getTransactions(User user) {
        return transactionRepository.getTransactionsByUserId(user.getUserId());
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
                createTransactionRequest.description(),
                createTransactionRequest.paymentMethod().orElse(null)
        );
    }

    public Transaction editTransaction(EditTransactionRequest editTransactionRequest, User user) {
        Transaction transaction = findUserTransaction(editTransactionRequest.id(), user.getUserId());
        setNewFieldsToTransaction(transaction, editTransactionRequest);
        transactionValidator.validateTransaction(transaction);
        return transactionRepository.upsertTransaction(transaction);
    }

    private Transaction findUserTransaction(String transactionId, String userId) {
        Transaction transaction = transactionRepository
                .getTransactionById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction with id " + transactionId + " not found"));

        if (!transaction.getUser().getUserId().equals(userId)) {
            throw new TransactionNotFoundException(String.format("User %s does not have transaction %s", userId, transactionId));
        }

        return transaction;
    }

    private void setNewFieldsToTransaction(Transaction transaction, EditTransactionRequest editTransactionRequest) {
        transaction.setName(editTransactionRequest.name());
        transaction.setType(editTransactionRequest.type());
        transaction.setValue(editTransactionRequest.value());
        transaction.setCurrency(editTransactionRequest.currency());
        transaction.setCategory(editTransactionRequest.category());
        transaction.setDescription(editTransactionRequest.description());
        transaction.setPaymentMethod(editTransactionRequest.paymentMethod().orElse(null));
    }
}
