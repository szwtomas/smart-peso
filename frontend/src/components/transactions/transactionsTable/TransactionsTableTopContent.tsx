import { Button, Selection, useDisclosure } from "@nextui-org/react";
import { RowsPerPageSelector } from "./RowsPerPageSelector";
import { TransactionTypeSelectorDropdown } from "./filters/TransactionTypeSelectorDropdown";
import { TransactionTableFilterInput } from "./filters/TransactionTableFilterInput";
import { PlusIcon } from "../../icons/Icons";
import { NewTransactionModal } from "../modal/NewTransactionModal";
import { TransactionCurrencySelectorDropdown } from "./filters/TransactionCurrencySelectorDropdown";
import { TransactionFilterOption } from "./filters/transactionFilters";
import { CreateTransactionFormData } from "../../../services/TransactionService";
import { TransactionCategoryFilter } from "./filters/TransactionCategoryFilter";

interface TransactionTableTopContentProps {
  nameFilterValue: string;
  onNameFilterClear: () => void;
  onNameSearchChange: (value?: string) => void;
  categoryFilterValue: string;
  onCategoryFilterClear: () => void;
  onCategorySearchChange: (value?: string) => void;
  transactionTypeFilter: Selection;
  setTransactionTypeFilter: React.Dispatch<React.SetStateAction<Selection>>;
  transactionTypeOptions: TransactionFilterOption[];
  transactionCurrencyFilter: Selection;
  setTransactionCurrencyFilter: React.Dispatch<React.SetStateAction<Selection>>;
  transactionCurrencyOptions: TransactionFilterOption[];
  transactionCount: number;
  onRowsPerPageChange: (e: React.ChangeEvent<HTMLSelectElement>) => void;
  onCreateTransaction: (transaction: CreateTransactionFormData) => void;
}

export function TransactionsTableTopContent(
  props: TransactionTableTopContentProps
) {
  const { isOpen, onOpen, onOpenChange } = useDisclosure();

  return (
    <div className="flex flex-col gap-4">
      <div className="flex justify-between gap-3 items-end">
        <TransactionTableFilterInput
          onSearchChange={props.onNameSearchChange}
          filterValue={props.nameFilterValue}
          onClear={props.onNameFilterClear}
        />
        <TransactionCategoryFilter
          filterValue={props.categoryFilterValue}
          onSearchChange={props.onCategorySearchChange}
          onClear={props.onCategoryFilterClear}
        />
        <div className="flex gap-3">
          <TransactionTypeSelectorDropdown
            transactionTypeFilter={props.transactionTypeFilter}
            transactionTypeOptions={props.transactionTypeOptions}
            setTransactionTypeFilter={props.setTransactionTypeFilter}
          />
          <TransactionCurrencySelectorDropdown
            transactionCurrencyFilter={props.transactionCurrencyFilter}
            transactionCurrencyOptions={props.transactionCurrencyOptions}
            setTransactionCurrencyFilter={props.setTransactionCurrencyFilter}
          />
          <Button
            color="primary"
            endContent={<PlusIcon />}
            size="lg"
            variant="shadow"
            onPress={onOpen}
            radius="sm"
          >
            Nueva Transacción
          </Button>
          <NewTransactionModal
            onOpenChange={onOpenChange}
            isOpen={isOpen}
            onCreateTransaction={props.onCreateTransaction}
          />
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
