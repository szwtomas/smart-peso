package com.smartpeso.repositories;

import com.smartpeso.model.Transaction;
import com.smartpeso.model.dto.transaction.TransactionDTO;
import com.smartpeso.repositories.exceptions.DeleteTransactionException;
import com.smartpeso.repositories.exceptions.TransactionCreationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TransactionRepositoryTest {
    @Mock
    private JdbcTemplate jdbcTemplateMock;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createTransaction_givenInsertionIsSuccessful_shouldNotThrowException() {
        int userId = 123;
        TransactionDTO transactionToCreate = createTransactionDTO();

        when(jdbcTemplateMock.update(any(), any(), any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(1);

        TransactionRepository unit = new TransactionRepository(jdbcTemplateMock);
        unit.createTransaction(transactionToCreate, userId);
    }

    @Test
    public void createTransaction_givenInsertionFails_shouldThrowException() {
        int userId = 123;
        TransactionDTO transactionToCreate = createTransactionDTO();

        when(jdbcTemplateMock.update(any(), any(), any(), any(), any(), any(), any(), any(), any(), any())).thenReturn(0);

        TransactionRepository unit = new TransactionRepository(jdbcTemplateMock);

        assertThrows(TransactionCreationException.class, () -> unit.createTransaction(transactionToCreate, userId));
    }

    @Test
    public void updateTransaction_givenUpdateIsSuccessful_shouldNotThrowException() {
        Transaction transactionToUpdate = createTransaction();
        TransactionRepository unit = new TransactionRepository(jdbcTemplateMock);

        unit.updateTransaction(transactionToUpdate);

        verify(jdbcTemplateMock).update(
                anyString(),
                eq(transactionToUpdate.getName()),
                eq(Timestamp.valueOf(transactionToUpdate.getDate())),
                eq(transactionToUpdate.getType()),
                eq(transactionToUpdate.getCurrency()),
                eq(transactionToUpdate.getValue()),
                eq(transactionToUpdate.getCategory()),
                eq(transactionToUpdate.getDescription()),
                eq(transactionToUpdate.getPaymentMethod()),
                eq(transactionToUpdate.getTransactionId())
        );
    }

    @Test
    public void getTransactions_givenUserHasNoTransactions_shouldReturnEmptyList() {
        int userId = 123;
        when(jdbcTemplateMock.query(anyString(), any(TransactionRowMapper.class), eq(userId))).thenReturn(new ArrayList<>());

        TransactionRepository unit = new TransactionRepository(jdbcTemplateMock);

        List<Transaction> transactions = unit.getUserTransactions(userId);

        assertTrue(transactions.isEmpty());
    }

    @Test
    public void getTransactions_givenUserHasTransactions_shouldTransactionsList() {
        int userId = 123;
        when(jdbcTemplateMock.query(anyString(), any(TransactionRowMapper.class), eq(userId)))
                .thenReturn(Arrays.asList(createTransaction(1), createTransaction(2), createTransaction(3)));

        TransactionRepository unit = new TransactionRepository(jdbcTemplateMock);

        List<Transaction> transactions = unit.getUserTransactions(userId);

        assertEquals(3, transactions.size());
        assertEquals(1, transactions.get(0).getTransactionId());
        assertEquals(2, transactions.get(1).getTransactionId());
        assertEquals(3, transactions.get(2).getTransactionId());
    }

    @Test
    public void getTransactionById_givenExistingTransactionId_shouldReturnTransaction() {
        int existingTransactionId = 667;
        Transaction existingTransaction = createTransaction(existingTransactionId);

        when(jdbcTemplateMock.queryForObject(
                anyString(),
                any(TransactionRowMapper.class),
                eq(existingTransactionId))
        ).thenReturn(existingTransaction);

        TransactionRepository unit = new TransactionRepository(jdbcTemplateMock);

        Optional<Transaction> actual = unit.getTransactionById(existingTransactionId);

        assertTrue(actual.isPresent());
        assertEquals(667, actual.get().getTransactionId());
    }

    @Test
    public void getTransactionById_givenNonExistingTransactionId_shouldReturnEmptyOptional() {
        int nonExistingTransactionId = 887;
        when(jdbcTemplateMock.queryForObject(
                anyString(),
                any(TransactionRowMapper.class),
                eq(nonExistingTransactionId))
        ).thenThrow(new RuntimeException("Transaction not found"));

        TransactionRepository unit = new TransactionRepository(jdbcTemplateMock);

        Optional<Transaction> actual = unit.getTransactionById(nonExistingTransactionId);

        assertTrue(actual.isEmpty());
    }

    @Test
    public void deleteTransaction_givenDeletionResultIsOk_shouldNotThrowException() {
        int transactionId = 123;
        when(jdbcTemplateMock.update(anyString(), eq(transactionId))).thenReturn(1);

        TransactionRepository unit = new TransactionRepository(jdbcTemplateMock);
        unit.deleteTransaction(transactionId);
    }

    @Test
    public void deleteTransaction_givenDeletionResultIsNotOk_shouldThrowException() {
        int transactionId = 123;
        when(jdbcTemplateMock.update(anyString(), eq(transactionId))).thenReturn(0);

        TransactionRepository unit = new TransactionRepository(jdbcTemplateMock);
        assertThrows(DeleteTransactionException.class, () -> unit.deleteTransaction(transactionId));
    }




    private Transaction createTransaction() {
        return new Transaction(
                1,
                1,
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

    private Transaction createTransaction(int id) {
        return new Transaction(
                id,
                1,
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

    private TransactionDTO createTransactionDTO() {
        return new TransactionDTO(
                "Salary Paycheck",
                "income",
                "USD",
                Optional.empty(),
                1000,
                "salary",
                "This month paycheck",
                LocalDateTime.now()
        );
    }
}
