package com.smartpeso.transaction;

import com.smartpeso.transaction.validator.TransactionValidator;
import com.smartpeso.transaction.exception.TransactionValidationException;
import com.smartpeso.transaction.model.dto.TransactionData;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TransactionValidatorTest {
    @Test
    public void validateTransaction_givenValidTransaction_itShouldValidateCorrectly() {
        TransactionData transaction = new TransactionData(
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
    public void validateTransaction_givenTransactionTypeIsNotIncomeOrExpense_itShouldThrowValidationException() {
        TransactionData transaction = new TransactionData(
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
        assertThrows(TransactionValidationException.class, () -> unit.validateTransaction(transaction));
    }
}
