import { SortDescriptor } from "@nextui-org/react";
import { Transaction } from "../../../context/TransactionContext";

export function sortTransactions(transactions: Transaction[], sortDescriptor: SortDescriptor): Transaction[] {
    return [...transactions].sort((a: Transaction, b: Transaction) => {
      if (sortDescriptor.column === "date") {
        const first = new Date(a.date).getTime();
        const second = new Date(b.date).getTime();
        const cmp = first < second ? -1 : first > second ? 1 : 0;
        return sortDescriptor.direction === "descending" ? -cmp : cmp;
      }

      const first = a[sortDescriptor.column as keyof Transaction] as number;
      const second = b[sortDescriptor.column as keyof Transaction] as number;
      const cmp = first < second ? -1 : first > second ? 1 : 0;

      return sortDescriptor.direction === "descending" ? -cmp : cmp;
    });
}
