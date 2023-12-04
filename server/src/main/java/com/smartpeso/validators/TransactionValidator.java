package com.smartpeso.validators;

import com.smartpeso.model.Transaction;
import lombok.NonNull;
import org.springframework.stereotype.Service;

@Service
public class TransactionValidator {
    public void validateTransaction(@NonNull Transaction transaction) {
        if (transaction.getUser() == null) {
            throw new TransactionValidationException("User must exist");
        }
    }
}
