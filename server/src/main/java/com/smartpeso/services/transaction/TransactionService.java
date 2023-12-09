package com.smartpeso.services.transaction;

import com.smartpeso.model.Transaction;
import com.smartpeso.model.User;
import com.smartpeso.model.dto.transaction.EditTransactionRequest;
import com.smartpeso.model.dto.transaction.TransactionDTO;
import com.smartpeso.repositories.TransactionRepository;
import com.smartpeso.validators.TransactionValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionValidator transactionValidator;

    public TransactionService(TransactionRepository transactionRepository, TransactionValidator transactionValidator) {
        this.transactionRepository = transactionRepository;
        this.transactionValidator = transactionValidator;
    }

    public Transaction createTransaction(TransactionDTO transactionDTO, User user) {
        Transaction transaction = createTransactionModel(transactionDTO, user.getUserId());
        transactionValidator.validateTransaction(transaction);
        return transactionRepository.upsertTransaction(transaction);
    }

    public List<Transaction> getTransactions(User user) {
        return transactionRepository.getTransactionsByUserId(user.getUserId());
    }

    private String createTransactionId() {
        return UUID.randomUUID().toString();
    }

    private Transaction createTransactionModel(TransactionDTO createTransactionRequest, String userId) {
        return new Transaction(
                createTransactionId(),
                userId,
                createTransactionRequest.name(),
                createTransactionRequest.date(),
                createTransactionRequest.type(),
                createTransactionRequest.currency(),
                createTransactionRequest.value(),
                createTransactionRequest.category(),
                createTransactionRequest.description(),
                createTransactionRequest.paymentMethod().orElse(null)
        );
    }

    public Transaction editTransaction(EditTransactionRequest editTransactionRequest, User user) {
        Transaction transaction = findUserTransaction(editTransactionRequest.transactionId(), user.getUserId());
        setNewFieldsToTransaction(transaction, editTransactionRequest);
        transactionValidator.validateTransaction(transaction);
        return transactionRepository.upsertTransaction(transaction);
    }

    public void deleteTransaction(String transactionId, User user) {
        Transaction transactionToDelete = findUserTransaction(transactionId, user.getUserId());
        transactionRepository.deleteTransaction(transactionToDelete);
    }

    private Transaction findUserTransaction(String transactionId, String userId) {
        Transaction transaction = transactionRepository
                .getTransactionById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction with transactionId " + transactionId + " not found"));

        if (!transaction.getUserId().equals(userId)) {
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
        transaction.setDate(editTransactionRequest.date());
        transaction.setPaymentMethod(editTransactionRequest.paymentMethod().orElse(null));
    }
}
