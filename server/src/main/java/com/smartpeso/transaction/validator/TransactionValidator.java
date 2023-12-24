package com.smartpeso.transaction.validator;

import com.smartpeso.transaction.model.dto.TransactionData;
import com.smartpeso.transaction.exception.TransactionValidationException;
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
