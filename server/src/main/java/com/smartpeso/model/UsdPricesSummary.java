package com.smartpeso.model;

public record UsdPricesSummary(
        UsdPrices today,
        UsdPrices yesterday,
        UsdPrices weekAgo,
        UsdPrices monthAgo,
        UsdPrices yearAgo) {
}
