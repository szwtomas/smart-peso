package com.smartpeso.repositories.exceptions;

public class DeleteTransactionException extends RuntimeException {
    public DeleteTransactionException(String message) {
        super(message);
    }
}
