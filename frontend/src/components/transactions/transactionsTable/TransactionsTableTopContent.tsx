import { Button, Selection } from "@nextui-org/react";
import { TransactionTypeOption } from "./columnData";
import { RowsPerPageSelector } from "./RowsPerPageSelector";
import { TransactioTypeSelectorDropdown } from "./TransactionTypeSelectorDropdown";
import { TransactionTableFilterInput } from "./TransactionTableFilterInput";
import { PlusIcon } from "./Icons";

interface TransactionTableTopContentProps {
  filterValue: string;
  onClear: () => void;
  onSearchChange: (value?: string) => void;
  transactionTypeFilter: Selection;
  setTransactionTypeFilter: React.Dispatch<React.SetStateAction<Selection>>;
  transactionTypeOptions: TransactionTypeOption[];
  transactionCount: number;
  onRowsPerPageChange: (e: React.ChangeEvent<HTMLSelectElement>) => void;
}

export function TransactionsTableTopContent(
  props: TransactionTableTopContentProps
) {
  return (
    <div className="flex flex-col gap-4">
      <div className="flex justify-between gap-3 items-end">
        <TransactionTableFilterInput
          onSearchChange={props.onSearchChange}
          filterValue={props.filterValue}
          onClear={props.onClear}
        />
        <div className="flex gap-3">
          <TransactioTypeSelectorDropdown
            transactionTypeFilter={props.transactionTypeFilter}
            transactionTypeOptions={props.transactionTypeOptions}
            setTransactionTypeFilter={props.setTransactionTypeFilter}
          />
          <Button
            color="primary"
            endContent={<PlusIcon />}
            size="lg"
            variant="shadow"
          >
            Nueva Transacción
          </Button>
        </div>
      </div>
      <div className="flex justify-between items-center">
        <span className="text-default-400 text-small">
          Total {props.transactionCount} transacciones
        </span>
        <RowsPerPageSelector onRowsPerPageChange={props.onRowsPerPageChange} />
      </div>
    </div>
  );
}
