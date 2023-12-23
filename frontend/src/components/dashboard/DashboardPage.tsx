import { PricesCards } from "./PricesCards";

export function DashboardPage() {
  return (
    <>
      <h1 className="text-3xl text-primary font-black mb-5 text-center">
        Cotizaciones del Dólar
      </h1>
      <div>
        <PricesCards />
      </div>
    </>
  );
}
