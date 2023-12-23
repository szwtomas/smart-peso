import { Card, CardBody } from "@nextui-org/react";
import { BanknotesIcon } from "@heroicons/react/24/outline";

export interface CardPriceProps {
  title: string;
  date: string;
  price: number;
  diff: number;
}

export function CardPrice(props: CardPriceProps) {
  return (
    <>
      <Card className="rounded-xl min-w-[250px]">
        <CardBody className="pt-6 px-6">
          <div className="flex flex-row justify-left gap-3">
            <BanknotesIcon className="h-8 w-8" />
            <div className="flex flex-col ">
              <p className="text-xl">{props.title}</p>
              <p className="text-xs mt-1 text-gray-500">{props.date}</p>
            </div>
          </div>
          <div className="flex flex-row gap-6 py-4 align-center justify-center">
            <span className="text-3xl font-black">${props.price}</span>
            <span
              className={`text-${
                props.diff > 0 ? "green" : "red"
              }-600 text-lg pt-1`}
            >
              {props.diff > 0 ? "+" : "-"}{" "}
              {props.diff > 0 ? props.diff : props.diff * -1}%
            </span>
          </div>
        </CardBody>
      </Card>
    </>
  );
}
