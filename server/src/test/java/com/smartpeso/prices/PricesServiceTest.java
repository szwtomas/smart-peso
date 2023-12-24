package com.smartpeso.prices;

import com.smartpeso.prices.model.UsdPrices;
import com.smartpeso.prices.model.UsdPricesSummary;
import com.smartpeso.prices.dal.PricesRepository;
import com.smartpeso.prices.PricesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

public class PricesServiceTest {
    @Mock
    private PricesRepository pricesRepositoryMock;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    final private Date today = formatter.parse("2023-12-22");
    final private Date yesterday = formatter.parse("2023-12-21");
    final private Date lastWeek = formatter.parse("2023-12-15");
    final private Date lastMonth = formatter.parse("2023-11-22");
    final private Date lastYear = formatter.parse("2022-12-22");
    final private UsdPrices todayPrices = new UsdPrices(today, 800, 850, 900, 950);
    final private UsdPrices yesterdayPrices = new UsdPrices(yesterday, 790, 860, 890, 930);
    final private UsdPrices lastWeekPrices = new UsdPrices(lastWeek, 700, 800, 800, 805);
    final private UsdPrices lastMonthPrices = new UsdPrices(lastMonth, 350, 900, 930, 1010);
    final private UsdPrices lastYearPrices = new UsdPrices(lastYear, 120, 350, 360, 400);

    public PricesServiceTest() throws ParseException {
    }

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getUSDPricesSummary_givenAllValuesAreRetrievedCorrectlyAndWeekday_itShouldBuildSummaryWithRetrievedValues() {
        ZoneId argentinaZone = ZoneId.of("America/Argentina/Buenos_Aires");
        LocalDate mockLocalDate = LocalDate.of(2023, 12, 22);
        MockedStatic<LocalDate> mockedStatic = mockStatic(LocalDate.class);
        mockedStatic.when(() -> LocalDate.now(eq(argentinaZone))).thenReturn(mockLocalDate);
        when(pricesRepositoryMock.getCurrentUsdPrices()).thenReturn(todayPrices);
        when(pricesRepositoryMock.getPricesFromDaysAgo(eq(1))).thenReturn(yesterdayPrices);
        when(pricesRepositoryMock.getPricesFromDaysAgo(eq(7))).thenReturn(lastWeekPrices);
        when(pricesRepositoryMock.getPricesFromDaysAgo(eq(30))).thenReturn(lastMonthPrices);
        when(pricesRepositoryMock.getPricesFromDaysAgo(eq(365))).thenReturn(lastYearPrices);

        PricesService unit = new PricesService(pricesRepositoryMock);

        UsdPricesSummary actual = unit.getUSDPricesSummary();
        mockedStatic.close();

        assertEquals(800, actual.today().official());
        assertEquals(790, actual.yesterday().official());
        assertEquals(700, actual.weekAgo().official());
        assertEquals(350, actual.monthAgo().official());
        assertEquals(120, actual.yearAgo().official());
    }

    @Test
    public void getUSDPricesSummary_givenAllValuesAreRetrievedCorrectlyAndWeekend_itShouldBuildSummaryWithRetrievedValues() {
        ZoneId argentinaZone = ZoneId.of("America/Argentina/Buenos_Aires");
        LocalDate mockLocalDate = LocalDate.of(2023, 12, 24);
        MockedStatic<LocalDate> mockedStatic = mockStatic(LocalDate.class);
        mockedStatic.when(() -> LocalDate.now(eq(argentinaZone))).thenReturn(mockLocalDate);
        when(pricesRepositoryMock.getCurrentUsdPrices()).thenReturn(todayPrices);
        when(pricesRepositoryMock.getPricesFromDaysAgo(eq(3))).thenReturn(yesterdayPrices);
        when(pricesRepositoryMock.getPricesFromDaysAgo(eq(7))).thenReturn(lastWeekPrices);
        when(pricesRepositoryMock.getPricesFromDaysAgo(eq(30))).thenReturn(lastMonthPrices);
        when(pricesRepositoryMock.getPricesFromDaysAgo(eq(365))).thenReturn(lastYearPrices);

        PricesService unit = new PricesService(pricesRepositoryMock);
        mockedStatic.close();

        UsdPricesSummary actual = unit.getUSDPricesSummary();

        assertEquals(800, actual.today().official());
        assertEquals(790, actual.yesterday().official());
        assertEquals(700, actual.weekAgo().official());
        assertEquals(350, actual.monthAgo().official());
        assertEquals(120, actual.yearAgo().official());
    }
}
