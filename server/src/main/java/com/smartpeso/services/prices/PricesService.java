package com.smartpeso.services.prices;

import com.smartpeso.model.UsdPrices;
import com.smartpeso.model.UsdPricesSummary;
import com.smartpeso.repositories.PricesRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;

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

    private int getDaysSinceLastBusinessDay() {
        ZoneId argentinaZone = ZoneId.of("America/Argentina/Buenos_Aires");
        return switch (LocalDate.now(argentinaZone).getDayOfWeek()) {
            case SATURDAY, SUNDAY -> 3;
            default -> 1;
        };
    }
}
