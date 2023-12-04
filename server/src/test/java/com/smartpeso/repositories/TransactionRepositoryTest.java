package com.smartpeso.repositories;

import com.smartpeso.model.Transaction;
import com.smartpeso.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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

    @Test
    public void getTransactions_givenUserHasNoTransactions_shouldReturnEmptyList() {
        when(mongoTemplateMock.find(any(Query.class), eq(Transaction.class), eq("transactions"))).thenReturn(new ArrayList<>());

        TransactionRepository unit = new TransactionRepository(mongoTemplateMock);

        List<Transaction> transactions = unit.getTransactionsByUserId("someId");

        assertTrue(transactions.isEmpty());
    }

    @Test
    public void getTransactions_givenUserHasTransactions_shouldTransactionsList() {

        when(mongoTemplateMock.find(any(Query.class), eq(Transaction.class), eq("transactions")))
                .thenReturn(Arrays.asList(createTransaction("id1"), createTransaction("id2"), createTransaction("id3")));

        TransactionRepository unit = new TransactionRepository(mongoTemplateMock);

        List<Transaction> transactions = unit.getTransactionsByUserId("someId");

        assertEquals(3, transactions.size());
        assertEquals("id1", transactions.get(0).getTransactionId());
        assertEquals("id2", transactions.get(1).getTransactionId());
        assertEquals("id3", transactions.get(2).getTransactionId());
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

    private Transaction createTransaction(String id) {
        return new Transaction(
                id,
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
