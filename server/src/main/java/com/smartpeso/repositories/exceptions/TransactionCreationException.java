package com.smartpeso.repositories.exceptions;

public class TransactionCreationException extends RuntimeException {
    public TransactionCreationException(String message) {
        super(message);
    }
}
