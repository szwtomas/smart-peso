package com.smartpeso.controllers;

import com.smartpeso.model.UsdPrices;
import com.smartpeso.services.PricesService;
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

    @GetMapping("/usd")
    public ResponseEntity<?> getUsdPrices() {
        try {
            UsdPrices usdPrices = pricesService.getUsdPrices();
            return new ResponseEntity<>(usdPrices, HttpStatus.OK);
        } catch(Exception e) {
            log.error("Error getting usd prices: " + e.getMessage());
            return new ResponseEntity<>("Failed getting usd prices", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
