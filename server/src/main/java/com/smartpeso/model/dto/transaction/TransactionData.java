package com.smartpeso.model.dto.transaction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class TransactionData {
    private String name;
    private LocalDateTime date;
    private String type;
    private String currency;
    private double value;
    private String category;
    private String description;
    private String paymentMethod;

    public static TransactionData fromDTO(TransactionDTO dto) {
        return new TransactionData(
                dto.name(),
                dto.date(),
                dto.type(),
                dto.currency(),
                dto.value(),
                dto.category(),
                dto.description(),
                dto.paymentMethod().orElse(null)
        );
    }

    public static TransactionData fromEditRequest(EditTransactionRequest editTransactionRequest) {
        return new TransactionData(
                editTransactionRequest.name(),
                editTransactionRequest.date(),
                editTransactionRequest.type(),
                editTransactionRequest.currency(),
                editTransactionRequest.value(),
                editTransactionRequest.category(),
                editTransactionRequest.description(),
                editTransactionRequest.paymentMethod().orElse(null)
        );
    }
}
