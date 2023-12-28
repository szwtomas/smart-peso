import Chart, { Props } from "react-apexcharts";

export interface CurrencyPlotData {
  name: string;
  data: number[];
}

export interface CurrencyPlotProps {
  official: CurrencyPlotData;
  mep: CurrencyPlotData;
  ccl: CurrencyPlotData;
  blue: CurrencyPlotData;
  options?: ApexCharts.ApexOptions;
}

export function PricesPlot(props: CurrencyPlotProps) {
  const series: Props["series"] = [
    props.official,
    props.mep,
    props.ccl,
    props.blue,
  ];

  return (
    <div className="w-[100%] z- mt-5">
      <Chart
        options={props.options}
        series={series}
        type="area"
        height={"460"}
      />
    </div>
  );
}
