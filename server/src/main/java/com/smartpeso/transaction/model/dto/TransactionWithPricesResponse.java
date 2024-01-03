package com.smartpeso.transaction.model.dto;

import com.smartpeso.transaction.model.TransactionWithPrices;

import java.util.List;

public record TransactionWithPricesResponse(List<TransactionWithPrices> data) {
}
