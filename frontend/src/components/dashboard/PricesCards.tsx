import { CardPrice } from "./PriceCard";

export function PricesCards() {
  return (
    <>
      <div className="flex flex-col lg:flex-row gap-2 lg:gap-3 xl:gap-5 justify-center mt-4">
        <CardPrice
          title="Dólar Oficial"
          date="23/12/2023"
          price={824}
          dailyDiff={1.0}
          weeklyDiff={-1.2}
          monthlyDiff={4.3}
          yearlyDiff={122}
        />
        <CardPrice
          title="Dólar MEP"
          date="23/12/2023"
          price={947}
          dailyDiff={2.3}
          weeklyDiff={-0.2}
          monthlyDiff={-4.3}
          yearlyDiff={122}
        />
        <CardPrice
          title="Dólar CCL"
          date="23/12/2023"
          price={956}
          dailyDiff={-1.4}
          weeklyDiff={-0.2}
          monthlyDiff={-4.3}
          yearlyDiff={122}
        />
        <CardPrice
          title="Dólar Blue"
          date="23/12/2023"
          price={995}
          dailyDiff={4.3}
          weeklyDiff={1.2}
          monthlyDiff={4.3}
          yearlyDiff={122}
        />
      </div>
    </>
  );
}
