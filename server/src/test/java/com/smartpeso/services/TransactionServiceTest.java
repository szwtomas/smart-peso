package com.smartpeso.services;

import com.smartpeso.model.Transaction;
import com.smartpeso.model.User;
import com.smartpeso.model.dto.transaction.EditTransactionRequest;
import com.smartpeso.model.dto.transaction.TransactionDTO;
import com.smartpeso.model.dto.transaction.TransactionData;
import com.smartpeso.repositories.TransactionRepository;
import com.smartpeso.services.transaction.TransactionNotFoundException;
import com.smartpeso.services.transaction.TransactionService;
import com.smartpeso.validators.TransactionValidationException;
import com.smartpeso.validators.TransactionValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {
    @Mock
    TransactionRepository transactionRepositoryMock;
    @Mock
    TransactionValidator transactionValidatorMock;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createTransaction_givenValidTransactionAndNoRepositoryErrors_shouldReturnCreatedTransaction() {
        TransactionDTO transactionDTO = getTransactionDTO();
        User user = getUser();

        doNothing().when(transactionRepositoryMock).createTransaction(any(TransactionDTO.class), eq(user.getUserId()));

        TransactionService unit = new TransactionService(transactionRepositoryMock, transactionValidatorMock);

        unit.createTransaction(transactionDTO, user);

        verify(transactionValidatorMock).validateTransaction(any(TransactionData.class));
        verify(transactionRepositoryMock).createTransaction(any(TransactionDTO.class), eq(user.getUserId()));
    }

    @Test
    public void createTransaction_givenValidationFails_shouldThrowTransactionValidationException() {
        doThrow(new TransactionValidationException("validation error")).when(transactionValidatorMock).validateTransaction(any(TransactionData.class));
        TransactionService unit = new TransactionService(transactionRepositoryMock, transactionValidatorMock);
        assertThrows(TransactionValidationException.class, () -> unit.createTransaction(getTransactionDTO(), getUser()));
    }

    @Test
    public void getTransactionByUser_shouldReturnOnlyUserTransactions() {
        int userId = 123;
        int otherUserId = 468;
        when(transactionRepositoryMock.getUserTransactions(userId)).thenReturn(Arrays.asList(createTransaction(222, userId), createTransaction(333, userId), createTransaction(444, userId)));
        when(transactionRepositoryMock.getUserTransactions(otherUserId)).thenReturn(List.of(createTransaction(900, otherUserId)));

        TransactionService unit = new TransactionService(transactionRepositoryMock, transactionValidatorMock);
        List<Transaction> firstUserTransactions = unit.getTransactions(getUser(userId));
        List<Transaction> secondUserTransactions = unit.getTransactions(getUser(otherUserId));

        assertEquals(firstUserTransactions.size(), 3);
        assertEquals(222, firstUserTransactions.get(0).getTransactionId());
        assertEquals(333, firstUserTransactions.get(1).getTransactionId());
        assertEquals(444, firstUserTransactions.get(2).getTransactionId());

        assertEquals(secondUserTransactions.size(), 1);
        assertEquals(900, secondUserTransactions.get(0).getTransactionId());
    }

    @Test
    public void editTransaction_givenExistingTransactionAndValidRequest_shouldReturnUpdatedTransaction() {
        int userId = 123;
        int transactionId = 432;
        User user = getUser(userId);

        EditTransactionRequest editTransactionRequest = getEditTransactionRequest();
        Transaction existingTransaction = createTransaction(transactionId, userId);

        when(transactionRepositoryMock.getTransactionById(eq(editTransactionRequest.transactionId()))).thenReturn(Optional.of(existingTransaction));
        doNothing().when(transactionRepositoryMock).updateTransaction(any(Transaction.class));

        TransactionService unit = new TransactionService(transactionRepositoryMock, transactionValidatorMock);
        unit.editTransaction(editTransactionRequest, user);

        verify(transactionRepositoryMock).getTransactionById(eq(editTransactionRequest.transactionId()));
        verify(transactionValidatorMock).validateTransaction(any(TransactionData.class));
        verify(transactionRepositoryMock).updateTransaction(eq(existingTransaction));
    }

    @Test
    public void editTransaction_givenTransactionDoesNotExist_shouldThrowTransactionNotFoundException() {
        int userId = 777;
        User user = getUser(userId);

        EditTransactionRequest editTransactionRequest = getEditTransactionRequest();

        when(transactionRepositoryMock.getTransactionById(eq(editTransactionRequest.transactionId()))).thenReturn(Optional.empty());

        TransactionService unit = new TransactionService(transactionRepositoryMock, transactionValidatorMock);

        assertThrows(TransactionNotFoundException.class, () -> unit.editTransaction(editTransactionRequest, user));
    }

    @Test
    public void editTransaction_givenTransactionExistsButBelongsToDifferentUser_shouldThrowTransactionNotFoundException() {
        int transactionId = 555;
        int userId = 123;
        int otherUserId = 31;
        User user = getUser();
        user.setUserId(userId);

        EditTransactionRequest editTransactionRequest = getEditTransactionRequest();
        Transaction existingTransaction = createTransaction(transactionId, otherUserId);

        when(transactionRepositoryMock.getTransactionById(eq(editTransactionRequest.transactionId()))).thenReturn(Optional.of(existingTransaction));

        TransactionService unit = new TransactionService(transactionRepositoryMock, transactionValidatorMock);

        assertThrows(TransactionNotFoundException.class, () -> unit.editTransaction(editTransactionRequest, user));
    }

    @Test
    public void deleteTransaction_givenExistingTransactionAndCorrectUser_shouldNotThrowException() {
        int transactionId = 555;
        int userId = 123;
        User user = getUser(userId);

        Transaction existingTransaction = createTransaction(transactionId, userId);

        when(transactionRepositoryMock.getTransactionById(eq(transactionId))).thenReturn(Optional.of(existingTransaction));

        TransactionService unit = new TransactionService(transactionRepositoryMock, transactionValidatorMock);
        unit.deleteTransaction(transactionId, user);

        verify(transactionRepositoryMock).deleteTransaction(eq(existingTransaction.getTransactionId()));
    }

    @Test
    public void deleteTransaction_givenNonExistingTransactionAndCorrectUser_shouldNotThrowException() {
        User user = getUser();

        when(transactionRepositoryMock.getTransactionById(eq(333))).thenReturn(Optional.empty());

        TransactionService unit = new TransactionService(transactionRepositoryMock, transactionValidatorMock);

        assertThrows(TransactionNotFoundException.class, () -> unit.deleteTransaction(333, user));
    }

    @Test
    public void deleteTransaction_givenUserDoesNotHaveTheTransaction_shouldNotThrowException() {
        int userId = 123;
        User user = getUser(userId);

        Transaction existingTransaction = createTransaction(444, 999);

        when(transactionRepositoryMock.getTransactionById(eq(444))).thenReturn(Optional.of(existingTransaction));

        TransactionService unit = new TransactionService(transactionRepositoryMock, transactionValidatorMock);

        assertThrows(TransactionNotFoundException.class, () -> unit.deleteTransaction(444, user));
    }

    private TransactionDTO getTransactionDTO() {
        return new TransactionDTO(
                "Salary Paycheck",
                "income",
                "USD",
                Optional.of("cash"),
                1000.0,
                "Salary",
                "This month paycheck",
                LocalDateTime.now()
        );
    }

    private Transaction createTransaction(int id, int userId) {
        return new Transaction(
                id,
                userId,
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

    private EditTransactionRequest getEditTransactionRequest() {
        return new EditTransactionRequest(
                123,
                "Updated Salary Paycheck",
                "income",
                "USD",
                Optional.of("cash"),
                1500.0,
                "Updated Salary",
                "Updated this month paycheck",
                LocalDateTime.now()
        );
    }

    private User getUser() {
        return new User(555, "john.doe@mail.com", "password", "salt","user", "John", "Doe");
    }

    private User getUser(int userId) {
        User user = getUser();
        user.setUserId(userId);
        return user;
    }
}
