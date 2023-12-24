package com.smartpeso.prices.model;

public record MonthlyPriceEntry(int year, int month, double official, double mep, double ccl, double blue) {
}
