package com.smartpeso.model;

import java.util.Date;

public record UsdPrices(Date date, double official, double mep, double ccl, double blue) {
}
