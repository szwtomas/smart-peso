package com.smartpeso.transaction.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.smartpeso.config.DateParser;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;


public record TransactionDTO(
        String name,
        String type,
        String currency,
        Optional<String> paymentMethod,
        double value,
        String category,
        String description,
        @JsonDeserialize(using = DateParser.class)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDateTime date
) implements Serializable {
}
