package com.smartpeso.prices;

import com.smartpeso.prices.model.MonthlyUSDPrices;
import com.smartpeso.prices.model.UsdPricesSummary;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/prices")
public class PricesController {
    private final PricesService pricesService;

    public PricesController(PricesService pricesService) {
        this.pricesService = pricesService;
    }

    @GetMapping("/usd/summary")
    public ResponseEntity<?> getPricesSummary() {
        try {
            UsdPricesSummary summary = pricesService.getUSDPricesSummary();
            return new ResponseEntity<>(summary, HttpStatus.OK);
        } catch(Exception e) {
            log.error("Error getting usd summary: " + e.getMessage());
            return new ResponseEntity<>("Failed getting usd prices summary", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/usd/monthly")
    public ResponseEntity<?> getMonthlyUSDPrices() {
        try {
            MonthlyUSDPrices monthlyPrices = pricesService.getMonthlyUSDPrices();
            return new ResponseEntity<>(monthlyPrices, HttpStatus.OK);
        } catch(Exception e) {
            log.error("Error getting usd summary: " + e.getMessage());
            return new ResponseEntity<>("Failed getting usd prices summary", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
