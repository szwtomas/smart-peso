import { useCallback, useMemo, useState } from "react";
import { Transaction, transactionData } from "../Transaction";
import {
  Button,
  Dropdown,
  DropdownItem,
  DropdownMenu,
  DropdownTrigger,
  Table,
  TableBody,
  TableCell,
  TableColumn,
  TableHeader,
  TableRow,
} from "@nextui-org/react";
import { EllipsisVerticalIcon } from "@heroicons/react/24/outline";
import { columns } from "./columnData";

const ROWS_PER_PAGE = 5;

function dateToString(date: Date): string {
  return `${date.getDate()}/${date.getMonth() + 1}/${date.getFullYear()}`;
}

export function TransactionContainer() {
  const [page, setPage] = useState(1);
  const pageCount = Math.ceil(transactionData.length / ROWS_PER_PAGE);

  const transactionsInPage = useMemo(() => {
    const start = (page - 1) * ROWS_PER_PAGE;
    const end = start + ROWS_PER_PAGE;

    return transactionData.slice(start, end);
  }, [page]);

  const renderCell = useCallback(
    (transaction: Transaction, columnKey: React.Key) => {
      const cellValue = transaction[columnKey as keyof Transaction];
      switch (columnKey) {
        case "id":
          return <p>{cellValue as string}</p>;
        case "name":
          return <p>{cellValue as string}</p>;
        case "type":
          return <p>{cellValue === "income" ? "Ingreso" : "Gasto"}</p>;
        case "date":
          return <p>{dateToString(cellValue as Date)}</p>;
        case "amount":
          return (
            <p
              className={
                transaction.type === "income"
                  ? "text-green-600"
                  : "text-red-600"
              }
            >
              ${cellValue as string}
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
          return <p>Null</p>;
      }
    },

    []
  );
  const onNextPage = useCallback(() => {
    if (page < pageCount) {
      setPage(page + 1);
    }
  }, [page, pageCount]);

  const onPreviousPage = useCallback(() => {
    if (page > 1) {
      setPage(page - 1);
    }
  }, [page]);

  return (
    <>
      <Table
        aria-label="users-transactions"
        isHeaderSticky
        classNames={{
          wrapper: "max-h-[382px]",
        }}
      >
        <TableHeader columns={columns}>
          {columns.map((c) => {
            return <TableColumn key={c.uid}>{c.name}</TableColumn>;
          })}
        </TableHeader>
        <TableBody
          emptyContent={"No hay Transacciones"}
          items={transactionsInPage}
        >
          {(t) => {
            return (
              <TableRow key={t.id}>
                {(columnKey) => (
                  <TableCell>{renderCell(t, columnKey)}</TableCell>
                )}
              </TableRow>
            );
          }}
        </TableBody>
      </Table>
    </>
  );
}
