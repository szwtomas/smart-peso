import { useEffect, useState } from "react";
import { monthNumberToString } from "../../../utils/utils";
import { CurrencyPlotData, PricesPlot } from "./PricesPlot";
import { pricesService } from "../../../services";

function getMonthCategories() {
  const now = new Date();
  const currentMonthIndex = now.getMonth();
  const categories: string[] = [];
  console.log("Starting");
  for (let i = 0; i <= 12; i++) {
    const monthIndex = (currentMonthIndex + i) % 12;
    const year = monthIndex < i ? now.getFullYear() : now.getFullYear() - 1;
    const monthName = monthNumberToString(monthIndex);
    categories.push(`${monthName} ${year}`);
  }

  categories.push(`Hoy`);

  return categories;
}

const options: ApexCharts.ApexOptions = {
  chart: {
    type: "area",
    animations: {
      easing: "linear",
      speed: 500,
    },
    sparkline: {
      enabled: false,
    },
    brush: {
      enabled: false,
    },
    id: "basic-bar",
    fontFamily: "Inter, sans-serif",
    foreColor: "var(--nextui-colors-accents9)",
    stacked: true,
    toolbar: {
      show: false,
    },
  },

  xaxis: {
    categories: getMonthCategories(),
    labels: {
      show: true,
      style: {
        colors: "var(--nextui-colors-accents8)",
        fontFamily: "Inter, sans-serif",
      },
    },
    axisBorder: {
      color: "#007aff",
    },
    axisTicks: {
      color: "#007aff",
    },
  },
  yaxis: {
    labels: {
      show: false,
    },
  },
  tooltip: {
    enabled: true,
  },
  grid: {
    show: true,
    borderColor: "#cdcfd1",
    strokeDashArray: 1,
    position: "front",
  },
  stroke: {
    curve: "smooth",
    fill: {
      colors: ["red"],
    },
  },
};

export function UsdPricesPlot() {
  const [mep, setMep] = useState<CurrencyPlotData>({} as CurrencyPlotData);
  const [ccl, setCcl] = useState<CurrencyPlotData>({} as CurrencyPlotData);
  const [blue, setBlue] = useState<CurrencyPlotData>({} as CurrencyPlotData);
  const [official, setOfficial] = useState<CurrencyPlotData>(
    {} as CurrencyPlotData
  );

  useEffect(() => {
    const fetchPlotData = async () => {
      const prices = await pricesService.getMonthlyPrices();

      const officialResponse: CurrencyPlotData = {
        name: "Oficial",
        data: prices.official.map((price: number) => Math.round(price)),
      };

      const mepResponse: CurrencyPlotData = {
        name: "MEP",
        data: prices.mep.map((price: number) => Math.round(price)),
      };

      const cclResponse: CurrencyPlotData = {
        name: "CCL",
        data: prices.ccl.map((price: number) => Math.round(price)),
      };

      const blueResponse: CurrencyPlotData = {
        name: "Blue",
        data: prices.blue.map((price: number) => Math.round(price)),
      };

      setOfficial(officialResponse);
      setMep(mepResponse);
      setCcl(cclResponse);
      setBlue(blueResponse);
    };

    fetchPlotData();
  });

  return (
    <div className="ml-10 w-[90%] z-5 mt-5">
      <PricesPlot
        options={options}
        official={official}
        mep={mep}
        ccl={ccl}
        blue={blue}
      />
    </div>
  );
}
