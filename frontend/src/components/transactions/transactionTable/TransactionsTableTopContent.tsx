import {
  Button,
  Dropdown,
  DropdownItem,
  DropdownMenu,
  DropdownTrigger,
  Input,
  Selection,
} from "@nextui-org/react";
import { ChevronDownIcon, PlusIcon, SearchIcon } from "./Icons";
import { TransactionTypeOption } from "./columnData";
import { capitalize } from "../../../utils/utils";

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
        <Input
          isClearable
          startContent={<SearchIcon />}
          className="w-full"
          size="sm"
          placeholder="Buscar por nombre..."
          value={props.filterValue}
          onClear={() => props.onClear()}
          onValueChange={props.onSearchChange}
        />
        <div className="flex gap-3">
          <Dropdown>
            <DropdownTrigger className="hidden sm:flex">
              <Button endContent={<ChevronDownIcon />} variant="flat" size="lg">
                Tipo
              </Button>
            </DropdownTrigger>
            <DropdownMenu
              disallowEmptySelection
              aria-label="Table Columns"
              closeOnSelect={false}
              selectedKeys={props.transactionTypeFilter}
              selectionMode="multiple"
              onSelectionChange={props.setTransactionTypeFilter}
            >
              {props.transactionTypeOptions.map((transactionType) => (
                <DropdownItem key={transactionType.uid} className="capitalize">
                  {capitalize(transactionType.name)}
                </DropdownItem>
              ))}
            </DropdownMenu>
          </Dropdown>
          <Button
            color="primary"
            endContent={<PlusIcon />}
            size="lg"
            variant="shadow"
          >
            Agregar Nueva Transacción
          </Button>
        </div>
      </div>
      <div className="flex justify-between items-center">
        <span className="text-default-400 text-small">
          Total {props.transactionCount} transacciones
        </span>
        <label className="flex items-center text-default-400 text-small">
          Rows per page:
          <select
            className="bg-transparent outline-none text-default-400 text-small"
            onChange={props.onRowsPerPageChange}
          >
            <option value="5">5</option>
            <option value="10">10</option>
            <option value="15">15</option>
          </select>
        </label>
      </div>
    </div>
  );
}
