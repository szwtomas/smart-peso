package com.smartpeso.validators;

import com.smartpeso.model.Transaction;
import com.smartpeso.model.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TransactionValidatorTest {
    @Test
    public void validateTransaction_givenValidTransaction_itShouldValidateCorrectly() {
        Transaction transaction = new Transaction(
                "someId",
                "userId",
                "Salary Paycheck",
                LocalDateTime.now(),
                "income",
                "USD",
                1000.0,
                "Salary",
                "This month paycheck",
                null
        );

        TransactionValidator unit = new TransactionValidator();
        unit.validateTransaction(transaction);
    }

    @Test
    public void validateTransactions_givenNullUser_itShouldThrowTransactionValidationException() {
        Transaction transaction = new Transaction(
                "someId",
                null,
                "Salary Paycheck",
                LocalDateTime.now(),
                "income",
                "USD",
                1000.0,
                "Salary",
                "This month paycheck",
                null
        );

        TransactionValidator unit = new TransactionValidator();
        assertThrows(TransactionValidationException.class, () -> {
            unit.validateTransaction(transaction);
        });
    }

    @Test
    public void validateTransaction_givenTransactionTypeIsNotIncomeOrExpense_itShouldThrowValidationException() {
        Transaction transaction = new Transaction(
                "someId",
                null,
                "Salary Paycheck",
                LocalDateTime.now(),
                "invalid type",
                "USD",
                1000.0,
                "Salary",
                "This month paycheck",
                null
        );

        TransactionValidator unit = new TransactionValidator();
        assertThrows(TransactionValidationException.class, () -> {
            unit.validateTransaction(transaction);
        });
    }
}
