import {
  SortDescriptor,
  Table,
  TableBody,
  TableCell,
  TableColumn,
  TableHeader,
  TableRow,
  Selection,
} from "@nextui-org/react";
import { Dispatch } from "react";
import { Column } from "./columnData";
import { Transaction } from "../../../services/TransactionService";

export interface TransactionsTableProps {
  itemsToShow: Transaction[];
  columns: Column[];
  renderCell: (transaction: Transaction, columnKey: React.Key) => JSX.Element;
  topContent: JSX.Element;
  bottomContent: JSX.Element;
  sortDescriptor: SortDescriptor;
  setSortDescriptor: Dispatch<React.SetStateAction<SortDescriptor>>;
  setSelectedKeys: Dispatch<React.SetStateAction<Selection>>;
}

export function TransactionsTable(props: TransactionsTableProps) {
  return (
    <Table
      aria-label="users-transactions"
      isHeaderSticky
      classNames={{
        wrapper: "max-h-[382px]",
      }}
      selectionMode="multiple"
      onSelectionChange={props.setSelectedKeys}
      topContent={props.topContent}
      topContentPlacement="outside"
      bottomContent={props.bottomContent}
      bottomContentPlacement="outside"
      sortDescriptor={props.sortDescriptor}
      onSortChange={props.setSortDescriptor}
    >
      <TableHeader columns={props.columns}>
        {props.columns.map((c) => {
          return (
            <TableColumn key={c.uid} allowsSorting={c.sortable}>
              {c.name}
            </TableColumn>
          );
        })}
      </TableHeader>
      <TableBody
        emptyContent={"No hay Transacciones"}
        items={props.itemsToShow}
      >
        {(t: Transaction) => {
          return (
            <TableRow key={t.transactionId}>
              {(columnKey) => (
                <TableCell>{props.renderCell(t, columnKey)}</TableCell>
              )}
            </TableRow>
          );
        }}
      </TableBody>
    </Table>
  );
}
