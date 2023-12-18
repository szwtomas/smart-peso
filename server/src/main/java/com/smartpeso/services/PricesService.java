package com.smartpeso.services;

import com.smartpeso.model.UsdPrices;
import com.smartpeso.repositories.PricesRepository;
import org.springframework.stereotype.Service;

@Service
public class PricesService {
    private final PricesRepository pricesRepository;

    public PricesService(PricesRepository pricesRepository) {
        this.pricesRepository = pricesRepository;
    }

    public UsdPrices getUsdPrices() {
        return pricesRepository.getLastPrices();
    }
}
