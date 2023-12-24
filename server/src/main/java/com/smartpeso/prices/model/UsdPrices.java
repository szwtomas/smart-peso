package com.smartpeso.prices.model;

import java.util.Date;

public record UsdPrices(Date date, double official, double mep, double ccl, double blue) {
}
