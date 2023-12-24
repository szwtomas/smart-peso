import { useEffect, useState } from "react";
import { UsdPricesSummary } from "../../../services/PricesService";
import { CardPrice } from "./PriceCard";
import { pricesService } from "../../../services";
import { defaultSummary } from "./defaultUSDPriceSummary";
import { roundFloatNumber } from "../../../utils/utils";

function calculateDiff(today: number, oldValue: number): number {
  if (oldValue === 0) return 0;
  const diff = ((today - oldValue) / oldValue) * 100;
  return roundFloatNumber(diff);
}

export function PricesCards() {
  const [summaryPrices, setSummaryPrices] =
    useState<UsdPricesSummary>(defaultSummary);

  useEffect(() => {
    async function fetchPrices() {
      const pricesResponse = await pricesService.getUsdPricesSummary();
      setSummaryPrices(pricesResponse);
    }

    fetchPrices();
  }, []);

  return (
    <>
      <div className="flex flex-col lg:flex-row gap-2 lg:gap-3 xl:gap-5 justify-center mt-4">
        <CardPrice
          title="Dólar Oficial"
          date="23/12/2023"
          price={summaryPrices.today.official}
          dailyDiff={calculateDiff(
            summaryPrices.today.official,
            summaryPrices.yesterday.official
          )}
          weeklyDiff={calculateDiff(
            summaryPrices.today.official,
            summaryPrices.weekAgo.official
          )}
          monthlyDiff={calculateDiff(
            summaryPrices.today.official,
            summaryPrices.monthAgo.official
          )}
          yearlyDiff={calculateDiff(
            summaryPrices.today.official,
            summaryPrices.yearAgo.official
          )}
        />
        <CardPrice
          title="Dólar MEP"
          date="23/12/2023"
          price={summaryPrices.today.mep}
          dailyDiff={calculateDiff(
            summaryPrices.today.mep,
            summaryPrices.yesterday.mep
          )}
          weeklyDiff={calculateDiff(
            summaryPrices.today.mep,
            summaryPrices.weekAgo.mep
          )}
          monthlyDiff={calculateDiff(
            summaryPrices.today.mep,
            summaryPrices.monthAgo.mep
          )}
          yearlyDiff={calculateDiff(
            summaryPrices.today.mep,
            summaryPrices.yearAgo.mep
          )}
        />
        <CardPrice
          title="Dólar CCL"
          date="23/12/2023"
          price={summaryPrices.today.ccl}
          dailyDiff={calculateDiff(
            summaryPrices.today.ccl,
            summaryPrices.yesterday.ccl
          )}
          weeklyDiff={calculateDiff(
            summaryPrices.today.ccl,
            summaryPrices.weekAgo.ccl
          )}
          monthlyDiff={calculateDiff(
            summaryPrices.today.ccl,
            summaryPrices.monthAgo.ccl
          )}
          yearlyDiff={calculateDiff(
            summaryPrices.today.ccl,
            summaryPrices.yearAgo.ccl
          )}
        />
        <CardPrice
          title="Dólar Blue"
          date="23/12/2023"
          price={summaryPrices.today.blue}
          dailyDiff={calculateDiff(
            summaryPrices.today.blue,
            summaryPrices.yesterday.blue
          )}
          weeklyDiff={calculateDiff(
            summaryPrices.today.blue,
            summaryPrices.weekAgo.blue
          )}
          monthlyDiff={calculateDiff(
            summaryPrices.today.blue,
            summaryPrices.monthAgo.blue
          )}
          yearlyDiff={calculateDiff(
            summaryPrices.today.blue,
            summaryPrices.yearAgo.blue
          )}
        />
      </div>
    </>
  );
}
