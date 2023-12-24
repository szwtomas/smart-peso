import { Card, CardBody } from "@nextui-org/react";
import { CardFooterDiff } from "./CardFooterDiff";

export interface CardPriceProps {
  title: string;
  date: string;
  price: number;
  dailyDiff: number;
  weeklyDiff: number;
  monthlyDiff: number;
  yearlyDiff: number;
}

export function CardPrice(props: CardPriceProps) {
  const isUp = props.dailyDiff >= 0;
  return (
    <>
      <Card className="rounded-xl min-w-[200px] md:min-w-[340px]">
        <CardBody className="pt-6">
          <div className="px-6 flex flex-row justify-left gap-3">
            {props.dailyDiff === 0 ? (
              <span className="text-2xl text-gray-600">-</span>
            ) : isUp ? (
              <span className="text-2xl text-green-600">↑</span>
            ) : (
              <span className="text-2xl text-red-600">↓</span>
            )}
            <div className="flex flex-col ">
              <p className="text-xl">{props.title}</p>
              <p className="hidden lg:block text-xs mt-1 text-gray-500">
                {props.date}
              </p>
            </div>
          </div>
          <div className="px-6 flex flex-row gap-6 py-4 align-center justify-center">
            <span className="text-3xl font-black">${props.price}</span>
            <span
              className={`text-${
                props.dailyDiff === 0 ? "gray" : isUp ? "green" : "red"
              }-600 text-lg mt-2`}
            >
              {props.dailyDiff === 0 ? "" : isUp ? "+" : "-"}
              {props.dailyDiff === 0
                ? "0%"
                : isUp
                ? `${props.dailyDiff}%`
                : `${props.dailyDiff * -1}%`}
            </span>
          </div>
          <hr />
          <div className="mt-3 px-0 flex md:flex-row justify-around">
            <CardFooterDiff title="Semanal" diff={props.weeklyDiff} />
            <CardFooterDiff title="Mensual" diff={props.monthlyDiff} />
            <CardFooterDiff title="Anual" diff={props.yearlyDiff} />
          </div>
        </CardBody>
      </Card>
    </>
  );
}
