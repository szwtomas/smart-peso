package com.smartpeso.model.dto.transaction;
public record TransactionDTO(
        String name,
        String type,
        String currency,
        double value,
        String category,
        String description
) {
}
