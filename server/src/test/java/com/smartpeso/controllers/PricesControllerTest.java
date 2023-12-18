package com.smartpeso.controllers;

import com.smartpeso.model.UsdPrices;
import com.smartpeso.services.PricesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

public class PricesControllerTest {
    @Mock
    private PricesService pricesService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getUsdPrices_serviceReturnsPrices_shouldReturnPricesAndStatusOK() {
        UsdPrices prices = new UsdPrices(new Date(), 800, 905, 900, 1000);
        when(pricesService.getUsdPrices()).thenReturn(prices);

        PricesController unit = new PricesController(pricesService);

        ResponseEntity<?> actualResponse = unit.getUsdPrices();
        UsdPrices actualPrices = (UsdPrices) actualResponse.getBody();

        assertEquals(200, actualResponse.getStatusCode().value());
        assertNotNull(actualPrices);
        assertEquals(800, actualPrices.official());
        assertEquals(900, actualPrices.ccl());
        assertEquals(905, actualPrices.mep());
        assertEquals(1000, actualPrices.blue());
    }

    @Test
    public void getUsdPrices_serviceFails_shouldReturnInternalServerErrorStatus() {
        when(pricesService.getUsdPrices()).thenThrow(new RuntimeException("some error"));

        PricesController unit = new PricesController(pricesService);

        ResponseEntity<?> actualResponse = unit.getUsdPrices();

        assertEquals(500, actualResponse.getStatusCode().value());
    }
}
