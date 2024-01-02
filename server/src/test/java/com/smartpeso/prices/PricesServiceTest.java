package com.smartpeso.prices;

import com.smartpeso.prices.model.MonthlyPriceEntry;
import com.smartpeso.prices.model.MonthlyUSDPrices;
import com.smartpeso.prices.model.UsdPrices;
import com.smartpeso.prices.model.UsdPricesSummary;
import com.smartpeso.prices.dal.PricesRepository;
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
import java.util.List;

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
        UsdPricesSummary actual = unit.getUSDPricesSummary();

        mockedStatic.close();
        
        assertEquals(800, actual.today().official());
        assertEquals(790, actual.yesterday().official());
        assertEquals(700, actual.weekAgo().official());
        assertEquals(350, actual.monthAgo().official());
        assertEquals(120, actual.yearAgo().official());
    }

    @Test
    public void monthlyPrices_shouldReturnMonthlyListWithCurrentPriceAtEndForEachCurrencyType() {
        UsdPrices currentPrices = new UsdPrices(new Date(), 400, 400, 400, 400);
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

        when(pricesRepositoryMock.getCurrentUsdPrices()).thenReturn(currentPrices);
        when(pricesRepositoryMock.getMonthlyUSDPrices()).thenReturn(entries);

        PricesService unit = new PricesService(pricesRepositoryMock);

        MonthlyUSDPrices actual = unit.getMonthlyUSDPrices();

        List<Double> official = actual.getOfficialPrices();
        List<Double> mep = actual.getMepPrices();
        List<Double> ccl = actual.getCclPrices();
        List<Double> blue = actual.getBluePrices();

        assertEquals(13, official.size());
        assertEquals(13, mep.size());
        assertEquals(13, ccl.size());
        assertEquals(13, blue.size());

        assertEquals(400.0, official.get(12));
    }
}
