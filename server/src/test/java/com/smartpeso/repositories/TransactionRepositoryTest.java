package com.smartpeso.repositories;

import com.smartpeso.model.Transaction;
import com.smartpeso.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TransactionRepositoryTest {
    @Mock
    private MongoTemplate mongoTemplateMock;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createTransaction_givenInsertionIsSuccessful_shouldReturnSameTransactionAndCallSave() {
        Transaction transactionToCreate = createTransaction();
        when(mongoTemplateMock.save(eq(transactionToCreate), eq("transactions"))).thenReturn(transactionToCreate);
        TransactionRepository unit = new TransactionRepository(mongoTemplateMock);
        Transaction createdTransaction = unit.createTransaction(transactionToCreate);

        verify(mongoTemplateMock).save(eq(transactionToCreate), eq("transactions"));

        assertEquals("someId", createdTransaction.getTransactionId());
    }

    @Test
    public void createTransaction_givenInsertionFails_shouldThrowTransactionCreationInsertion() {
        Transaction transactionToCreate = createTransaction();
        when(mongoTemplateMock.save(eq(transactionToCreate), eq("transactions"))).thenThrow(new RuntimeException("some error"));
        TransactionRepository unit = new TransactionRepository(mongoTemplateMock);
        assertThrows(TransactionCreationException.class, () -> unit.createTransaction(transactionToCreate));
    }

    private Transaction createTransaction() {
        return new Transaction(
                "someId",
                new User("john.doe@mail.com", "password", "user", "John", "Doe"),
                "Salary Paycheck",
                new Date(),
                "income",
                "USD",
                1000.0,
                "Salary",
                "This month paycheck"
        );
    }
}
