import { PricesCards } from "./PricesCards";

export function DashboardPage() {
  const date = "23/12/2023";
  return (
    <>
      <h1 className="text-3xl text-primary font-black mb-5 text-center">
        Cotización Diaria
      </h1>
      <div>
        <PricesCards />
      </div>
    </>
  );
}
