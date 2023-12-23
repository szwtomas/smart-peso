import Chart, { Props } from "react-apexcharts";

const state: Props["series"] = [
  {
    name: "Oficial",
    data: [185, 194, 204, 216, 233, 253, 272, 290, 366, 367, 367, 368, 700],
  },
  {
    name: "MEP",
    data: [329, 357, 362, 402, 428, 469, 484, 509, 670, 709, 898, 930, 933],
  },
  {
    name: "CCL",
    data: [342, 362, 368, 406, 433, 479, 495, 560, 800, 829, 883, 991, 944],
  },
  {
    name: "Blue",
    data: [348, 376, 377, 392, 469, 490, 493, 570, 735, 800, 915, 955, 990],
  },
];

const options: Props["options"] = {
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
    categories: [
      "Enero",
      "Febrero",
      "Marzo",
      "Abril",
      "Mayo",
      "Junio",
      "Julio",
      "Agosto",
      "Septiembre",
      "Octubre",
      "Noviembre",
      "Diciembre",
      "Enero 2024",
    ],
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
  return (
    <div className="w-[100%] z- mt-5">
      <Chart options={options} series={state} type="area" height={"460"} />
    </div>
  );
}
