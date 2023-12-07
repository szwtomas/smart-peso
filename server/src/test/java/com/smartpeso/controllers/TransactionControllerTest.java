package com.smartpeso.controllers;

import com.smartpeso.model.Transaction;
import com.smartpeso.model.User;
import com.smartpeso.model.dto.transaction.EditTransactionRequest;
import com.smartpeso.model.dto.transaction.TransactionDTO;
import com.smartpeso.repositories.TransactionCreationException;
import com.smartpeso.services.transaction.TransactionNotFoundException;
import com.smartpeso.services.transaction.TransactionService;
import com.smartpeso.validators.TransactionValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TransactionControllerTest {
    @Mock
    TransactionService transactionServiceMock;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createTransaction_givenValidUserAndTransactionDTO_transactionIsCreated_shouldReturnCreatedCode() {
        User user = getUser();
        TransactionDTO transactionDTO = getTransactionDTO();
        Transaction transaction = transactionFromTransactionDTO(transactionDTO);

        when(transactionServiceMock.createTransaction(any(TransactionDTO.class), any(User.class))).thenReturn(transaction);

        TransactionController unit = new TransactionController(transactionServiceMock);

        ResponseEntity<?> actualResponse = unit.createTransaction(user, transactionDTO);
        Transaction createdTransaction = (Transaction) actualResponse.getBody();

        assertEquals(201, actualResponse.getStatusCode().value());
        assertNotNull(createdTransaction);
        assertEquals("someId", createdTransaction.getTransactionId());
    }

    @Test
    public void createTransaction_givenValidationFails_shouldReturnBadRequest() {
        User user = getUser();
        TransactionDTO transactionDTO = getTransactionDTO();

        when(transactionServiceMock.createTransaction(any(TransactionDTO.class), any(User.class))).thenThrow(new TransactionValidationException("validation error"));

        TransactionController unit = new TransactionController(transactionServiceMock);

        ResponseEntity<?> actualResponse = unit.createTransaction(user, transactionDTO);

        assertEquals(400, actualResponse.getStatusCode().value());
        assertEquals("validation error", actualResponse.getBody());
    }

    @Test
    public void createTransaction_givenUnexpectedException_shouldReturnInternalServerError() {
        User user = getUser();
        TransactionDTO transactionDTO = getTransactionDTO();

        when(transactionServiceMock.createTransaction(any(TransactionDTO.class), any(User.class))).thenThrow(new TransactionCreationException("creation error"));

        TransactionController unit = new TransactionController(transactionServiceMock);

        ResponseEntity<?> actualResponse = unit.createTransaction(user, transactionDTO);

        assertEquals(500, actualResponse.getStatusCode().value());
        assertEquals("creation error", actualResponse.getBody());
    }

    @Test
    public void getTransaction_givenTransactionsAreFetchedCorrectly_shouldReturnTransactionsAndStatusOK() {
        User user = getUser();
        when(transactionServiceMock.getTransactions(eq(user)))
                .thenReturn(Arrays.asList(getTransaction("id1"), getTransaction("id2")));
        TransactionController unit = new TransactionController(transactionServiceMock);

        ResponseEntity<?> actualResponse = unit.getUserTransactions(user);
        List<Transaction> actualTransactions = (List<Transaction>) actualResponse.getBody();

        assertEquals(200, actualResponse.getStatusCode().value());
        assertNotNull(actualTransactions);
        assertEquals(2, actualTransactions.size());
        assertEquals("id1", actualTransactions.get(0).getTransactionId());
        assertEquals("id2", actualTransactions.get(1).getTransactionId());
    }

    @Test
    public void getTransaction_givenTransactionFetchingFails_shouldReturnInternalServerError() {
        User user = getUser();
        when(transactionServiceMock.getTransactions(eq(user))).thenThrow(new RuntimeException("some error"));
        TransactionController unit = new TransactionController(transactionServiceMock);

        ResponseEntity<?> actualResponse = unit.getUserTransactions(user);
        String message = (String) actualResponse.getBody();

        assertEquals(500, actualResponse.getStatusCode().value());
        assertNotNull(message);
        assertEquals("Failed getting user transactions", message);
    }

    @Test
    public void editTransactionGivenExistingAndValidTransaction_shouldReturnStatusOk() {
        User user = getUser();
        EditTransactionRequest editTransactionRequest = getEditTransactionRequest();

        Transaction newTransaction = getTransaction(editTransactionRequest.id());
        newTransaction.setName(editTransactionRequest.name());

        when(transactionServiceMock.editTransaction(eq(editTransactionRequest), eq(user))).thenReturn(newTransaction);
        TransactionController unit = new TransactionController(transactionServiceMock);

        ResponseEntity<?> actualResponse = unit.editTransaction(user, editTransactionRequest);
        Transaction actualTransaction = (Transaction) actualResponse.getBody();

        verify(transactionServiceMock).editTransaction(eq(editTransactionRequest), eq(user));

        assertEquals(200, actualResponse.getStatusCode().value());
        assertNotNull(actualTransaction);
        assertEquals("Updated Salary Paycheck", actualTransaction.getName());
    }

    @Test
    public void editTransaction_GivenTransactionIsNotFound_shouldReturnNotFound() {
        User user = getUser();
        EditTransactionRequest editTransactionRequest = getEditTransactionRequest();

        when(transactionServiceMock.editTransaction(eq(editTransactionRequest), eq(user))).thenThrow(new TransactionNotFoundException("not found"));
        TransactionController unit = new TransactionController(transactionServiceMock);

        ResponseEntity<?> actualResponse = unit.editTransaction(user, editTransactionRequest);

        verify(transactionServiceMock).editTransaction(eq(editTransactionRequest), eq(user));

        assertEquals(404, actualResponse.getStatusCode().value());
    }

    @Test
    public void editTransaction_givenTransactionValidationFails_shouldReturnBadRequest() {
        User user = getUser();
        EditTransactionRequest editTransactionRequest = getEditTransactionRequest();

        when(transactionServiceMock.editTransaction(eq(editTransactionRequest), eq(user))).thenThrow(new TransactionValidationException("validation failed"));
        TransactionController unit = new TransactionController(transactionServiceMock);

        ResponseEntity<?> actualResponse = unit.editTransaction(user, editTransactionRequest);

        verify(transactionServiceMock).editTransaction(eq(editTransactionRequest), eq(user));

        assertEquals(400, actualResponse.getStatusCode().value());
    }

    @Test
    public void editTransaction_givenTransactionUpserFailsWithUnexpectedError_shouldReturnInternalServerError() {
        User user = getUser();
        EditTransactionRequest editTransactionRequest = getEditTransactionRequest();

        when(transactionServiceMock.editTransaction(eq(editTransactionRequest), eq(user))).thenThrow(new RuntimeException("something failed"));
        TransactionController unit = new TransactionController(transactionServiceMock);

        ResponseEntity<?> actualResponse = unit.editTransaction(user, editTransactionRequest);

        verify(transactionServiceMock).editTransaction(eq(editTransactionRequest), eq(user));

        assertEquals(500, actualResponse.getStatusCode().value());
    }

    private TransactionDTO getTransactionDTO() {
        return new TransactionDTO(
                "Salary Paycheck",
                "income",
                "USD",
                null,
                1000.0,
                "Salary",
                "This month paycheck"
        );
    }

    private Transaction transactionFromTransactionDTO(TransactionDTO transactionDTO) {
        return new Transaction(
                "someId",
                getUser(),
                transactionDTO.name(),
                new Date(),
                transactionDTO.type(),
                transactionDTO.currency(),
                transactionDTO.value(),
                transactionDTO.category(),
                transactionDTO.description(),
                null
        );
    }

    private Transaction getTransaction(String transactionId) {
        return new Transaction(
                transactionId,
                getUser(),
                "Shopping",
                new Date(),
                "expense",
                "usd",
                100.0,
                "Groceries",
                "Monthly groceries :)",
                null
        );
    }

    private User getUser() {
        return new User("john.doe@mail.com", "password", "user", "John", "Doe");
    }

    private EditTransactionRequest getEditTransactionRequest() {
        return new EditTransactionRequest(
                "someId",
                "Updated Salary Paycheck",
                "income",
                "USD",
                Optional.of("cash"),
                1500.0,
                "Updated Salary",
                "Updated this month paycheck"
        );
    }
}
