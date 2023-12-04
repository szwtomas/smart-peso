package com.smartpeso.services;

import com.smartpeso.model.Transaction;
import com.smartpeso.model.User;
import com.smartpeso.model.dto.transaction.TransactionDTO;
import com.smartpeso.repositories.TransactionRepository;
import com.smartpeso.services.transaction.TransactionService;
import com.smartpeso.validators.TransactionValidationException;
import com.smartpeso.validators.TransactionValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
        Transaction transaction = transactionFromTransactionDTO(transactionDTO);
        when(transactionRepositoryMock.createTransaction(any(Transaction.class))).thenReturn(transaction);

        TransactionService unit = new TransactionService(transactionRepositoryMock, transactionValidatorMock);

        Transaction createdTransaction = unit.createTransaction(transactionDTO, user);

        verify(transactionValidatorMock).validateTransaction(any(Transaction.class));

        assertEquals("someId", createdTransaction.getTransactionId());
        assertEquals("Salary Paycheck", createdTransaction.getName());
        assertEquals(1000.0, createdTransaction.getValue(), 0.01);
    }

    @Test
    public void createTransaction_givenValidationFails_shouldThrowTransactionValidationException() {
        doThrow(new TransactionValidationException("validation error")).when(transactionValidatorMock).validateTransaction(any(Transaction.class));
        TransactionService unit = new TransactionService(transactionRepositoryMock, transactionValidatorMock);
        assertThrows(TransactionValidationException.class, () -> unit.createTransaction(getTransactionDTO(), getUser()));
    }

    @Test
    public void getTransactionByUser_shouldReturnOnlyUserTransactions() {
        when(transactionRepositoryMock.getTransactionsByUserId("someId")).thenReturn(Arrays.asList(createTransaction("id1"), createTransaction("id2"), createTransaction("id3")));
        when(transactionRepositoryMock.getTransactionsByUserId("otherId")).thenReturn(List.of(createTransaction("id4")));

        TransactionService unit = new TransactionService(transactionRepositoryMock, transactionValidatorMock);
        List<Transaction> firstUserTransactions = unit.getTransactions(getUser("someId"));
        List<Transaction> secondUserTransactions = unit.getTransactions(getUser("otherId"));

        assertEquals(firstUserTransactions.size(), 3);
        assertEquals("id1", firstUserTransactions.get(0).getTransactionId());
        assertEquals("id2", firstUserTransactions.get(1).getTransactionId());
        assertEquals("id3", firstUserTransactions.get(2).getTransactionId());

        assertEquals(secondUserTransactions.size(), 1);
        assertEquals("id4", secondUserTransactions.get(0).getTransactionId());
    }

    private TransactionDTO getTransactionDTO() {
        return new TransactionDTO(
                "Salary Paycheck",
                "income",
                "USD",
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
                transactionDTO.description()
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

    private User getUser() {
        return new User("john.doe@mail.com", "password", "user", "John", "Doe");
    }

    private User getUser(String userId) {
        User user = getUser();
        user.setUserId(userId);
        return user;
    }
}
