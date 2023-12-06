package com.smartpeso.model.dto.transaction;

import java.util.Optional;

public record TransactionDTO(
        String name,
        String type,
        String currency,
        Optional<String> paymentMethod,
        double value,
        String category,
        String description
) {
}
