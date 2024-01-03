package com.smartpeso.transaction.model;

import com.smartpeso.prices.model.UsdPrices;
import com.smartpeso.transaction.model.Transaction;

public record TransactionWithPrices(Transaction transaction, UsdPrices prices) {
}
