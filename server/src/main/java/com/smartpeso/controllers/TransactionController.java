package com.smartpeso.controllers;

import com.smartpeso.model.Transaction;
import com.smartpeso.model.User;
import com.smartpeso.model.dto.transaction.DeleteTransactionRequest;
import com.smartpeso.model.dto.transaction.EditTransactionRequest;
import com.smartpeso.model.dto.transaction.TransactionDTO;
import com.smartpeso.repositories.exceptions.TransactionCreationException;
import com.smartpeso.services.transaction.TransactionNotFoundException;
import com.smartpeso.services.transaction.TransactionService;
import com.smartpeso.validators.TransactionValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/transaction")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<?> createTransaction(
            @AuthenticationPrincipal User user,
            @RequestBody TransactionDTO createTransactionRequest
    ) {
        try {
            Transaction transaction = transactionService.createTransaction(createTransactionRequest, user);
            return new ResponseEntity<>(transaction, HttpStatus.CREATED);
        } catch (TransactionValidationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (TransactionCreationException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<?> getUserTransactions(@AuthenticationPrincipal User user) {
        try {
            List<Transaction> userTransactions = transactionService.getTransactions(user);
            return new ResponseEntity<>(userTransactions, HttpStatus.OK);
        } catch(Exception e) {
            log.error("Failed getting transactions for user " + user.getEmail() + ", error: " + e.getMessage());
            return new ResponseEntity<>("Failed getting user transactions", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping
    public ResponseEntity<?> editTransaction(
            @AuthenticationPrincipal User user,
            @RequestBody EditTransactionRequest editTransactionRequest) {
        try {
            Transaction transaction = transactionService.editTransaction(editTransactionRequest, user);
            log.info(String.format("Transaction with transactionId %s successfully edited", editTransactionRequest.transactionId()));
            return new ResponseEntity<>(transaction, HttpStatus.OK);
        } catch (TransactionNotFoundException e) {
            log.error(String.format("User %s tried to edit transaction [%s], but it does not exist", user.getEmail(), editTransactionRequest.transactionId()));
            return new ResponseEntity<>(String.format("Transaction %s does not exist", editTransactionRequest.transactionId()), HttpStatus.NOT_FOUND);
        } catch (TransactionValidationException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed editing transaction", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteTransaction(
            @AuthenticationPrincipal User user,
            @RequestBody DeleteTransactionRequest deleteTransactionRequest) {
        try {
            transactionService.deleteTransaction(deleteTransactionRequest.transactionId(), user);
            return new ResponseEntity<>("Transaction deleted successfully", HttpStatus.ACCEPTED);
        } catch (TransactionNotFoundException e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(String.format("Transaction %s does not exist", deleteTransactionRequest.transactionId()), HttpStatus.NOT_FOUND);
        } catch(Exception e) {
            log.error("Failed deleting transaction: " + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
