package com.smartpeso.validators;

import com.smartpeso.model.Transaction;
import com.smartpeso.model.User;
import com.smartpeso.model.dto.transaction.TransactionDTO;
import com.smartpeso.model.dto.transaction.TransactionData;
import lombok.NonNull;
import org.springframework.stereotype.Service;

@Service
public class TransactionValidator {
    public void validateTransaction(@NonNull TransactionData transaction) {
        validateTransactionType(transaction.getType());
    }

    private void validateTransactionType(String type) {
        String typeLower = type.toLowerCase();
        if (!(typeLower.equals("income") || typeLower.equals("expense"))) {
            throw new TransactionValidationException("Transaction type can only be income or expense");
        }
    }
}
