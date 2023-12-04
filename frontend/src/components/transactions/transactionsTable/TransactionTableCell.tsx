import { Button } from "@nextui-org/react";
import { dateToString } from "../../../utils/utils";
import { Transaction } from "../../../context/TransactionContext";

export interface TransactionTableCellProps {
  transaction: Transaction;
  columnKey: React.Key;
}

export function TransactionTableCell(props: TransactionTableCellProps) {
  const { transaction, columnKey } = props;
  const cellValue = transaction[columnKey as keyof Transaction];
  switch (columnKey) {
    case "id":
      return <p>{cellValue as string}</p>;
    case "name":
      return <p>{cellValue as string}</p>;
    case "type":
      return (
        <p
          className={
            transaction.type === "income" ? "text-green-600" : "text-red-600"
          }
        >
          {cellValue === "income" ? "Ingreso" : "Gasto"}
        </p>
      );
    case "date":
      return <p>{dateToString(cellValue as Date)}</p>;
    case "currency":
      return <p>{cellValue as string}</p>;
    case "value":
      return (
        <p
          className={
            transaction.type === "income" ? "text-green-600" : "text-red-600"
          }
        >
          ${cellValue.toString()}
        </p>
      );
    case "category":
      return <p>{cellValue as string}</p>;
    case "actions":
      return (
        <div>
          <Button size="sm" variant="light">
            <p className="text-xl text-primary">
              <b>+</b>
            </p>
          </Button>
        </div>
      );
    default:
      return <p className="text-red-600">Error</p>;
  }
}
