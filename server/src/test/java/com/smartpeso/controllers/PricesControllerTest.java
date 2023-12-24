package com.smartpeso.controllers;

import com.smartpeso.prices.model.MonthlyUSDPrices;
import com.smartpeso.prices.model.UsdPrices;
import com.smartpeso.prices.model.UsdPricesSummary;
import com.smartpeso.prices.PricesController;
import com.smartpeso.prices.PricesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PricesControllerTest {
    @Mock
    private PricesService pricesServiceMock;

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    final private Date today = formatter.parse("2023-12-22");
    final private Date yesterday = formatter.parse("2023-12-21");
    final private Date lastWeek = formatter.parse("2023-12-15");
    final private Date lastMonth = formatter.parse("2023-11-22");
    final private Date lastYear = formatter.parse("2022-12-22");

    public PricesControllerTest() throws ParseException {
    }

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getUsdSummary_givenServiceReturnsSummary_shouldReturnItAndStatusCodeOk() {
        UsdPricesSummary summary = new UsdPricesSummary(
                new UsdPrices(today, 800, 850, 900, 950),
                new UsdPrices(yesterday, 790, 860, 890, 930),
                new UsdPrices(lastWeek, 700, 800, 800, 805),
                new UsdPrices(lastMonth, 350, 900, 930, 1010),
                new UsdPrices(lastYear, 120, 350, 360, 400)
        );

        when(pricesServiceMock.getUSDPricesSummary()).thenReturn(summary);

        PricesController unit = new PricesController(pricesServiceMock);
        ResponseEntity<?> actualResponse = unit.getPricesSummary();
        UsdPricesSummary actualPrices = (UsdPricesSummary) actualResponse.getBody();

        assertEquals(200, actualResponse.getStatusCode().value());
        assertNotNull(actualPrices);
        assertEquals(800, actualPrices.today().official());
        assertEquals(790, actualPrices.yesterday().official());
        assertEquals(700, actualPrices.weekAgo().official());
        assertEquals(350, actualPrices.monthAgo().official());
        assertEquals(120, actualPrices.yearAgo().official());
    }

    @Test
    public void getUsdSummary_givenServiceFails_itShouldReturnInternalServerError() {
        when(pricesServiceMock.getUSDPricesSummary()).thenThrow(new RuntimeException("some error"));

        PricesController unit = new PricesController(pricesServiceMock);
        ResponseEntity<?> actualResponse = unit.getPricesSummary();

        assertEquals(500, actualResponse.getStatusCode().value());
    }

    @Test
    public void getUsdMonthlyAveragePrices_givenPricesCalculated_itShouldReturnPricesWithStatusOK() {
        MonthlyUSDPrices monthlyPrices = mock(MonthlyUSDPrices.class);
        when(monthlyPrices.getOfficialPrices()).thenReturn(List.of(100.0, 120.0, 130.0, 140.0));
        when(pricesServiceMock.getMonthlyUSDPrices()).thenReturn(monthlyPrices);

        PricesController unit = new PricesController(pricesServiceMock);

        ResponseEntity<?> actualResponse = unit.getMonthlyUSDPrices();

        MonthlyUSDPrices actualPrices = (MonthlyUSDPrices) actualResponse.getBody();
        assertEquals(200, actualResponse.getStatusCode().value());
        assertNotNull(actualPrices);
        assertEquals(4, actualPrices.getOfficialPrices().size());
    }

    @Test
    public void getUsdMonthlyAveragePrices_givenPricesCalculatioFails_itShouldReturnInternalServerError() {
        when(pricesServiceMock.getMonthlyUSDPrices()).thenThrow(new RuntimeException("Some error"));

        PricesController unit = new PricesController(pricesServiceMock);

        ResponseEntity<?> actualResponse = unit.getMonthlyUSDPrices();

        assertEquals(500, actualResponse.getStatusCode().value());
    }
}
