package com.smartpeso.services;

import com.smartpeso.model.UsdPrices;
import com.smartpeso.repositories.PricesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class PricesServiceTest {
    @Mock
    private PricesRepository pricesRepositoryMock;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void pricesRepositoryIsOk_shouldReturnTheSameUsdPrices() {
        UsdPrices prices = new UsdPrices(new Date(), 800, 900, 1000, 1100);
        when(pricesRepositoryMock.getLastPrices()).thenReturn(prices);

        PricesService unit = new PricesService(pricesRepositoryMock);
        UsdPrices actual = unit.getUsdPrices();

        assertEquals(800, actual.official());
        assertEquals(900, actual.mep());
        assertEquals(1000, actual.ccl());
        assertEquals(1100, actual.blue());
    }
}
