package com.smartpeso.validators;

import com.smartpeso.model.Transaction;
import com.smartpeso.model.User;
import lombok.NonNull;
import org.springframework.stereotype.Service;

@Service
public class TransactionValidator {
    public void validateTransaction(@NonNull Transaction transaction) {
        validateTransactionUser(transaction.getUserId());
        validateTransactionType(transaction.getType());
    }

    private void validateTransactionUser(String userId) {
        if (userId == null) {
            throw new TransactionValidationException("User must exist");
        }
    }

    private void validateTransactionType(String type) {
        String typeLower = type.toLowerCase();
        if (!(typeLower.equals("income") || typeLower.equals("expense"))) {
            throw new TransactionValidationException("Transaction type can only be income or expense");
        }
    }
}
