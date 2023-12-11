package com.smartpeso.services.transaction;

import com.smartpeso.model.Transaction;
import com.smartpeso.model.User;
import com.smartpeso.model.dto.transaction.EditTransactionRequest;
import com.smartpeso.model.dto.transaction.TransactionDTO;
import com.smartpeso.model.dto.transaction.TransactionData;
import com.smartpeso.repositories.TransactionRepository;
import com.smartpeso.validators.TransactionValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionValidator transactionValidator;

    public TransactionService(TransactionRepository transactionRepository, TransactionValidator transactionValidator) {
        this.transactionRepository = transactionRepository;
        this.transactionValidator = transactionValidator;
    }

    public void createTransaction(TransactionDTO transactionDTO, User user) {
        transactionValidator.validateTransaction(TransactionData.fromDTO(transactionDTO));
        transactionRepository.createTransaction(transactionDTO, user.getUserId());
    }

    public List<Transaction> getTransactions(User user) {
        return transactionRepository.getUserTransactions(user.getUserId());
    }

    public void editTransaction(EditTransactionRequest editTransactionRequest, User user) {
        transactionValidator.validateTransaction(TransactionData.fromEditRequest(editTransactionRequest));
        Transaction transaction = findUserTransaction(editTransactionRequest.transactionId(), user.getUserId());
        setNewFieldsToTransaction(transaction, editTransactionRequest);
        transactionRepository.updateTransaction(transaction);
    }

    public void deleteTransaction(int transactionId, User user) {
        Transaction transactionToDelete = findUserTransaction(transactionId, user.getUserId());
        transactionRepository.deleteTransaction(transactionToDelete.getTransactionId());
    }

    private Transaction findUserTransaction(int transactionId, int userId) {
        Transaction transaction = transactionRepository.getTransactionById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction with transactionId " + transactionId + " not found"));

        if (!(transaction.getUserId() == userId)) {
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
