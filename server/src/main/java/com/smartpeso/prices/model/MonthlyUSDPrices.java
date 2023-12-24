package com.smartpeso.prices.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class MonthlyUSDPrices {
    @JsonProperty("official")
    private final List<Double> officialPrices;
    @JsonProperty("mep")
    private final List<Double> mepPrices;
    @JsonProperty("ccl")
    private final List<Double> cclPrices;
    @JsonProperty("blue")
    private final List<Double> bluePrices;

    public MonthlyUSDPrices() {
        this.officialPrices = new ArrayList<>();
        this.mepPrices = new ArrayList<>();
        this.cclPrices = new ArrayList<>();
        this.bluePrices = new ArrayList<>();
    }

    public void addOfficial(double price) {
        officialPrices.add(price);
    }

    public void addMEP(double price) {
        mepPrices.add(price);
    }

    public void addCCL(double price) {
        cclPrices.add(price);
    }

    public void addBlue(double price) {
        bluePrices.add(price);
    }
}
