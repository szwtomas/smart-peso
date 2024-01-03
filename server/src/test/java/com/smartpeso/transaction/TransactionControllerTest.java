package com.smartpeso.transaction;

import com.smartpeso.prices.model.UsdPrices;
import com.smartpeso.transaction.model.Transaction;
import com.smartpeso.auth.model.User;
import com.smartpeso.transaction.model.TransactionWithPrices;
import com.smartpeso.transaction.model.dto.DeleteTransactionRequest;
import com.smartpeso.transaction.model.dto.EditTransactionRequest;
import com.smartpeso.transaction.model.dto.TransactionDTO;
import com.smartpeso.transaction.exception.DeleteTransactionException;
import com.smartpeso.transaction.exception.TransactionCreationException;
import com.smartpeso.transaction.TransactionController;
import com.smartpeso.transaction.exception.TransactionNotFoundException;
import com.smartpeso.transaction.TransactionService;
import com.smartpeso.transaction.exception.TransactionValidationException;
import com.smartpeso.transaction.model.dto.TransactionWithPricesResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class TransactionControllerTest {
    @Mock
    TransactionService transactionServiceMock;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createTransaction_givenValidUserAndTransactionDTO_transactionIsCreated_shouldReturnCreatedStatusCode() {
        User user = getUser();
        TransactionDTO transactionDTO = getTransactionDTO();

        doNothing().when(transactionServiceMock).createTransaction(any(TransactionDTO.class), any(User.class));

        TransactionController unit = new TransactionController(transactionServiceMock);

        ResponseEntity<?> actualResponse = unit.createTransaction(user, transactionDTO);

        assertEquals(201, actualResponse.getStatusCode().value());
    }

    @Test
    public void createTransaction_givenValidationFails_shouldReturnBadRequest() {
        User user = getUser();
        TransactionDTO transactionDTO = getTransactionDTO();

        doThrow(new TransactionValidationException("validation error")).when(transactionServiceMock).createTransaction(any(TransactionDTO.class), any(User.class));

        TransactionController unit = new TransactionController(transactionServiceMock);

        ResponseEntity<?> actualResponse = unit.createTransaction(user, transactionDTO);

        assertEquals(400, actualResponse.getStatusCode().value());
        assertEquals("validation error", actualResponse.getBody());
    }

    @Test
    public void createTransaction_givenUnexpectedException_shouldReturnInternalServerError() {
        User user = getUser();
        TransactionDTO transactionDTO = getTransactionDTO();

        doThrow(new TransactionCreationException("creation error")).when(transactionServiceMock).createTransaction(any(TransactionDTO.class), any(User.class));

        TransactionController unit = new TransactionController(transactionServiceMock);

        ResponseEntity<?> actualResponse = unit.createTransaction(user, transactionDTO);

        assertEquals(500, actualResponse.getStatusCode().value());
        assertEquals("creation error", actualResponse.getBody());
    }

    @Test
    public void getTransaction_givenTransactionsAreFetchedCorrectly_shouldReturnTransactionsAndStatusOK() {
        User user = getUser();
        when(transactionServiceMock.getTransactions(eq(user))).thenReturn(List.of(getTransaction(555), getTransaction(678)));
        TransactionController unit = new TransactionController(transactionServiceMock);

        ResponseEntity<?> actualResponse = unit.getUserTransactions(user);
        List<Transaction> actualTransactions = (List<Transaction>) actualResponse.getBody();

        assertEquals(200, actualResponse.getStatusCode().value());
        assertNotNull(actualTransactions);
        assertEquals(2, actualTransactions.size());
        assertEquals(555, actualTransactions.get(0).getTransactionId());
        assertEquals(678, actualTransactions.get(1).getTransactionId());
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

        doNothing().when(transactionServiceMock).editTransaction(eq(editTransactionRequest), eq(user));
        TransactionController unit = new TransactionController(transactionServiceMock);

        ResponseEntity<?> actualResponse = unit.editTransaction(user, editTransactionRequest);

        verify(transactionServiceMock).editTransaction(eq(editTransactionRequest), eq(user));

        assertEquals(200, actualResponse.getStatusCode().value());
    }

    @Test
    public void editTransaction_GivenTransactionIsNotFound_shouldReturnNotFound() {
        User user = getUser();
        EditTransactionRequest editTransactionRequest = getEditTransactionRequest();

        doThrow(new TransactionNotFoundException("not found")).when(transactionServiceMock).editTransaction(eq(editTransactionRequest), eq(user));
        TransactionController unit = new TransactionController(transactionServiceMock);

        ResponseEntity<?> actualResponse = unit.editTransaction(user, editTransactionRequest);

        verify(transactionServiceMock).editTransaction(eq(editTransactionRequest), eq(user));

        assertEquals(404, actualResponse.getStatusCode().value());
    }

    @Test
    public void editTransaction_givenTransactionValidationFails_shouldReturnBadRequest() {
        User user = getUser();
        EditTransactionRequest editTransactionRequest = getEditTransactionRequest();

        doThrow(new TransactionValidationException("validation failed")).when(transactionServiceMock).editTransaction(eq(editTransactionRequest), eq(user));
        TransactionController unit = new TransactionController(transactionServiceMock);

        ResponseEntity<?> actualResponse = unit.editTransaction(user, editTransactionRequest);

        verify(transactionServiceMock).editTransaction(eq(editTransactionRequest), eq(user));

        assertEquals(400, actualResponse.getStatusCode().value());
    }

    @Test
    public void editTransaction_givenTransactionUpsertFailsWithUnexpectedError_shouldReturnInternalServerError() {
        User user = getUser();
        EditTransactionRequest editTransactionRequest = getEditTransactionRequest();

        doThrow(new RuntimeException("something failed")).when(transactionServiceMock).editTransaction(eq(editTransactionRequest), eq(user));
        TransactionController unit = new TransactionController(transactionServiceMock);

        ResponseEntity<?> actualResponse = unit.editTransaction(user, editTransactionRequest);

        verify(transactionServiceMock).editTransaction(eq(editTransactionRequest), eq(user));

        assertEquals(500, actualResponse.getStatusCode().value());
    }

    @Test
    public void deleteTransaction_givenDeletionIsSuccessful_shouldReturnSuccessStatus() {
        int transactionId = 553;
        User user = getUser();
        DeleteTransactionRequest deleteRequest = new DeleteTransactionRequest(transactionId);

        TransactionController unit = new TransactionController(transactionServiceMock);

        ResponseEntity<?> actualResponse = unit.deleteTransaction(user, deleteRequest);

        assertEquals(202, actualResponse.getStatusCode().value());
    }

    @Test
    public void deleteTransaction_givenDeletionThrowsNotFound_shouldReturnNotFoundStatus() {
        int transactionId = 553;
        User user = getUser();
        DeleteTransactionRequest deleteRequest = new DeleteTransactionRequest(transactionId);

        doThrow(new TransactionNotFoundException("not found")).when(transactionServiceMock).deleteTransaction(eq(transactionId), eq(user));

        TransactionController unit = new TransactionController(transactionServiceMock);

        ResponseEntity<?> actualResponse = unit.deleteTransaction(user, deleteRequest);

        assertEquals(404, actualResponse.getStatusCode().value());
    }

    @Test
    public void deleteTransaction_givenDeletionThrowsDeletionException_shouldReturnInternalServerErrorStatus() {
        int transactionId = 553;
        User user = getUser();
        DeleteTransactionRequest deleteRequest = new DeleteTransactionRequest(transactionId);

        doThrow(new DeleteTransactionException("some error")).when(transactionServiceMock).deleteTransaction(eq(transactionId), eq(user));

        TransactionController unit = new TransactionController(transactionServiceMock);

        ResponseEntity<?> actualResponse = unit.deleteTransaction(user, deleteRequest);

        assertEquals(500, actualResponse.getStatusCode().value());
    }

    @Test
    public void getTransactionsWithPrices_givenServiceReturnsResponse_itShouldReturnOkStatus() {
        User user = getUser();
        List<TransactionWithPrices> transactions = List.of(
                new TransactionWithPrices(getTransaction(1), getUsdPrices(1000)),
                new TransactionWithPrices(getTransaction(2), getUsdPrices(2000)),
                new TransactionWithPrices(getTransaction(3), getUsdPrices(3000))
        );

        when(transactionServiceMock.getTransactionsWithPrices(eq(user))).thenReturn(transactions);

        TransactionController unit = new TransactionController(transactionServiceMock);
        ResponseEntity<?> actualResponse = unit.getUserTransactionsWithPrices(user);

        TransactionWithPricesResponse actual = (TransactionWithPricesResponse) actualResponse.getBody();

        assertEquals(200, actualResponse.getStatusCode().value());
        assertNotNull(actual);
        assertEquals(actual.data().size(), 3);
        assertEquals(1, actual.data().get(0).transaction().getTransactionId());
        assertEquals(1000, actual.data().get(0).prices().official());
        assertEquals(2, actual.data().get(1).transaction().getTransactionId());
        assertEquals(2000, actual.data().get(1).prices().official());
        assertEquals(3, actual.data().get(2).transaction().getTransactionId());
        assertEquals(3000, actual.data().get(2).prices().official());
    }

    @Test
    public void getTransactionsWithPrices_givenServiceFails_itShouldReturnInternalServerError() {
        User user = getUser();
        when(transactionServiceMock.getTransactionsWithPrices(eq(user))).thenThrow(new RuntimeException("some error"));

        TransactionController unit = new TransactionController(transactionServiceMock);
        ResponseEntity<?> actualResponse = unit.getUserTransactionsWithPrices(user);

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
                "This month paycheck",
                LocalDateTime.now()
        );
    }

    private Transaction getTransaction(int transactionId) {
        return new Transaction(
                transactionId,
                444,
                "Shopping",
                LocalDateTime.now(),
                "expense",
                "usd",
                100.0,
                "Groceries",
                "Monthly groceries :)",
                null
        );
    }

    private User getUser() {
        return new User(444, "john.doe@mail.com", "password", "salt", "user", "John", "Doe");
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

    private UsdPrices getUsdPrices(int officialValue) {
        return new UsdPrices(new Date(), officialValue, 2000, 3000, 4000);
    }
}
