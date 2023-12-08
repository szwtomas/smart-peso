package com.smartpeso.repositories;

import com.mongodb.client.result.DeleteResult;
import com.smartpeso.model.Transaction;
import com.smartpeso.repositories.exceptions.DeleteTransactionException;
import com.smartpeso.repositories.exceptions.TransactionCreationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.time.LocalDateTime;
import java.util.*;

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
        Transaction createdTransaction = unit.upsertTransaction(transactionToCreate);

        verify(mongoTemplateMock).save(eq(transactionToCreate), eq("transactions"));

        assertEquals("someId", createdTransaction.getTransactionId());
    }

    @Test
    public void createTransaction_givenInsertionFails_shouldThrowTransactionCreationInsertion() {
        Transaction transactionToCreate = createTransaction();
        when(mongoTemplateMock.save(eq(transactionToCreate), eq("transactions"))).thenThrow(new RuntimeException("some error"));
        TransactionRepository unit = new TransactionRepository(mongoTemplateMock);
        assertThrows(TransactionCreationException.class, () -> unit.upsertTransaction(transactionToCreate));
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

    @Test
    public void getTransactionById_givenExistingTransactionId_shouldReturnTransaction() {
        String existingTransactionId = "existingId";
        Transaction existingTransaction = createTransaction(existingTransactionId);
        Query query = new Query(Criteria.where("_id").is(existingTransactionId));

        when(mongoTemplateMock.findOne(query, Transaction.class, "transactions")).thenReturn(existingTransaction);

        TransactionRepository unit = new TransactionRepository(mongoTemplateMock);

        Optional<Transaction> actual = unit.getTransactionById(existingTransactionId);

        assertTrue(actual.isPresent());
        assertEquals(existingTransactionId, actual.get().getTransactionId());
    }

    @Test
    public void getTransactionById_givenNonExistingTransactionId_shouldReturnEmptyOptional() {
        String nonExistingTransactionId = "nonExistingId";
        Query query = new Query(Criteria.where("_id").is(nonExistingTransactionId));

        when(mongoTemplateMock.findOne(query, Transaction.class, "transactions")).thenReturn(null);

        TransactionRepository unit = new TransactionRepository(mongoTemplateMock);

        Optional<Transaction> result = unit.getTransactionById(nonExistingTransactionId);

        assertFalse(result.isPresent());
    }

    @Test
    public void deleteTransaction_givenDeletionResultIsOk_shouldNotThrowException() {
        Transaction transaction = createTransaction();

        when(mongoTemplateMock.remove(eq(transaction), eq("transactions"))).thenReturn(new DeleteResult() {
            @Override
            public boolean wasAcknowledged() {
                return false;
            }

            @Override
            public long getDeletedCount() {
                return 1;
            }
        });

        TransactionRepository unit = new TransactionRepository(mongoTemplateMock);
        unit.deleteTransaction(transaction);

        verify(mongoTemplateMock).remove(eq(transaction), eq("transactions"));
    }

    @Test
    public void deleteTransaction_givenDeletionResultIsNotOk_shouldThrowTransactionDeletionException() {
        Transaction transaction = createTransaction();

        when(mongoTemplateMock.remove(eq(transaction), eq("transactions"))).thenReturn(new DeleteResult() {
            @Override
            public boolean wasAcknowledged() {
                return false;
            }

            @Override
            public long getDeletedCount() {
                return 0;
            }
        });

        TransactionRepository unit = new TransactionRepository(mongoTemplateMock);

        assertThrows(DeleteTransactionException.class, () -> unit.deleteTransaction(transaction));
    }

    private Transaction createTransaction() {
        return new Transaction(
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
    }

    private Transaction createTransaction(String id) {
        return new Transaction(
                id,
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
    }
}
