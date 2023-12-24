package com.smartpeso.prices.model;

public record UsdPricesSummary(
        UsdPrices today,
        UsdPrices yesterday,
        UsdPrices weekAgo,
        UsdPrices monthAgo,
        UsdPrices yearAgo) {
}
