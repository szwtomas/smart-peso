package com.smartpeso.prices;

import com.smartpeso.prices.model.UsdPrices;
import com.smartpeso.prices.dal.PricesRepository;
import com.smartpeso.prices.dal.UsdPricesRowMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
}
