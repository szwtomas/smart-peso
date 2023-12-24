package com.smartpeso.prices;

import com.smartpeso.prices.dal.MonthlyPriceEntryRowMapper;
import com.smartpeso.prices.model.MonthlyPriceEntry;
import com.smartpeso.prices.model.UsdPrices;
import com.smartpeso.prices.dal.PricesRepository;
import com.smartpeso.prices.dal.UsdPricesRowMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

public class PricesRepositoryTest {
    @Mock
    JdbcTemplate jdbcTemplateMock;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getUsdPrices_givenQueryIsOK_shouldReturnExpectedPrices() {
        UsdPrices prices = new UsdPrices(new Date(), 800, 900, 1000, 1100);
        when(jdbcTemplateMock.queryForObject(anyString(), any(UsdPricesRowMapper.class))).thenReturn(prices);

        PricesRepository unit = new PricesRepository(jdbcTemplateMock);
        UsdPrices actual = unit.getCurrentUsdPrices();

        assertEquals(800, actual.official());
        assertEquals(900, actual.mep());
        assertEquals(1000, actual.ccl());
        assertEquals(1100, actual.blue());
    }

    @Test
    public void getMonthlyUSDPrices_shouldReturnOneEntryPerMonth() {
        List<MonthlyPriceEntry> entries = List.of(
                new MonthlyPriceEntry(2022, 12, 150, 300, 300, 300),
                new MonthlyPriceEntry(2023, 1, 150, 300, 300, 300),
                new MonthlyPriceEntry(2023, 2, 150, 300, 300, 300),
                new MonthlyPriceEntry(2023, 3, 150, 300, 300, 300),
                new MonthlyPriceEntry(2023, 4, 150, 300, 300, 300),
                new MonthlyPriceEntry(2023, 5, 150, 300, 300, 300),
                new MonthlyPriceEntry(2023, 6, 150, 300, 300, 300),
                new MonthlyPriceEntry(2023, 7, 150, 300, 300, 300),
                new MonthlyPriceEntry(2023, 8, 150, 300, 300, 300),
                new MonthlyPriceEntry(2023, 9, 150, 300, 300, 300),
                new MonthlyPriceEntry(2023, 10, 150, 300, 300, 300),
                new MonthlyPriceEntry(2023, 11, 150, 300, 300, 300)
        );

        doReturn(entries).when(jdbcTemplateMock).query(anyString(), any(MonthlyPriceEntryRowMapper.class));

        PricesRepository unit = new PricesRepository(jdbcTemplateMock);
        List<MonthlyPriceEntry> actualEntries = unit.getMonthlyUSDPrices();

        assertEquals(12, actualEntries.size());
    }
}
