import { UsdPricesPlot } from "./plot/UsdPricesPlot";
import { PricesCards } from "./pricesCards/PricesCards";

export function PricesPage() {
  return (
    <>
      <h1 className="text-3xl text-primary font-black mb-5 text-center">
        Cotización Diaria
      </h1>
      <div>
        <PricesCards />
        <UsdPricesPlot />
      </div>
    </>
  );
}
