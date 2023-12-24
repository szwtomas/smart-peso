package com.smartpeso.prices;

import com.smartpeso.prices.dal.PricesRepository;
import com.smartpeso.prices.model.MonthlyPriceEntry;
import com.smartpeso.prices.model.MonthlyUSDPrices;
import com.smartpeso.prices.model.UsdPrices;
import com.smartpeso.prices.model.UsdPricesSummary;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Service
public class PricesService {
    private final PricesRepository pricesRepository;

    public PricesService(PricesRepository pricesRepository) {
        this.pricesRepository = pricesRepository;
    }

    public UsdPricesSummary getUSDPricesSummary() {
        UsdPrices todayPrices = pricesRepository.getCurrentUsdPrices();
        int daysSinceLastBusinessDay = getDaysSinceLastBusinessDay();
        int daysInWeek = 7;
        int daysInMonth = 30;
        int daysInYear = 365;

        UsdPrices yesterdayPrices = pricesRepository.getPricesFromDaysAgo(daysSinceLastBusinessDay);
        UsdPrices lastWeekPrices = pricesRepository.getPricesFromDaysAgo(daysInWeek);
        UsdPrices lastMonthPrices = pricesRepository.getPricesFromDaysAgo(daysInMonth);
        UsdPrices lastYearPrices = pricesRepository.getPricesFromDaysAgo(daysInYear);
        return new UsdPricesSummary(todayPrices, yesterdayPrices, lastWeekPrices, lastMonthPrices, lastYearPrices);
    }

    public MonthlyUSDPrices getMonthlyUSDPrices() {
        MonthlyUSDPrices monthlyUSDPrices = new MonthlyUSDPrices();
        addEntriesToMonthlyPrices(monthlyUSDPrices);
        addTodayPricesToMonthlyPrices(monthlyUSDPrices);
        return monthlyUSDPrices;
    }

    private void addEntriesToMonthlyPrices(MonthlyUSDPrices prices) {
        List<MonthlyPriceEntry> pricesByMonth = pricesRepository.getMonthlyUSDPrices();

        for (MonthlyPriceEntry entry : pricesByMonth) {
            prices.addOfficial(entry.official());
            prices.addMEP(entry.mep());
            prices.addCCL(entry.ccl());
            prices.addBlue(entry.blue());
        }
    }

    private void addTodayPricesToMonthlyPrices(MonthlyUSDPrices prices) {
        UsdPrices todayPrices = pricesRepository.getCurrentUsdPrices();

        prices.addOfficial(todayPrices.official());
        prices.addMEP(todayPrices.mep());
        prices.addCCL(todayPrices.ccl());
        prices.addBlue(todayPrices.blue());
    }

    private int getDaysSinceLastBusinessDay() {
        ZoneId argentinaZone = ZoneId.of("America/Argentina/Buenos_Aires");
        return switch (LocalDate.now(argentinaZone).getDayOfWeek()) {
            case SATURDAY, SUNDAY -> 3;
            default -> 1;
        };
    }
}
