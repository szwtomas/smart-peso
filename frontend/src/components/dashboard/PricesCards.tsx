import { CardPrice } from "./PriceCard";

export function PricesCards() {
  return (
    <>
      <div className="flex flex-col lg:flex-row gap-2 lg:gap-3 xl:gap-5 justify-center mt-4">
        <CardPrice
          title="Dólar Oficial"
          date="23/12/2023"
          price={824}
          diff={1.0}
        />
        <CardPrice title="Dólar MEP" date="23/12/2023" price={947} diff={2.3} />
        <CardPrice
          title="Dólar CCL"
          date="23/12/2023"
          price={956}
          diff={-1.4}
        />
        <CardPrice
          title="Dólar Blue"
          date="23/12/2023"
          price={995}
          diff={4.3}
        />
        <CardPrice
          title="Dólar Tarjeta"
          date="23/12/2023"
          price={1310}
          diff={2.1}
        />
      </div>
    </>
  );
}
