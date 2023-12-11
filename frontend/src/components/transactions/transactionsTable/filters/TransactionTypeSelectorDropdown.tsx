import {
  Button,
  Dropdown,
  DropdownItem,
  DropdownMenu,
  DropdownTrigger,
  Selection,
} from "@nextui-org/react";
import { ChevronDownIcon } from "../../../icons/Icons";
import { capitalize } from "../../../../utils/utils";
import { TransactionFilterOption } from "./transactionFilters";

export interface TransactionTypeSelectorDropdownProps {
  transactionTypeFilter: Selection;
  setTransactionTypeFilter: React.Dispatch<React.SetStateAction<Selection>>;
  transactionTypeOptions: TransactionFilterOption[];
}

export function TransactionTypeSelectorDropdown(
  props: TransactionTypeSelectorDropdownProps
) {
  return (
    <Dropdown>
      <DropdownTrigger className="hidden md:flex">
        <Button
          endContent={<ChevronDownIcon />}
          variant="ghost"
          size="lg"
          radius="sm"
          className="bg-white text-primary"
          color="primary"
        >
          <div className="flex flex-row items-center">
            <p className="mr-3 ml-3">Tipo</p>
            <ChevronDownIcon />
          </div>
        </Button>
      </DropdownTrigger>
      <DropdownMenu
        disallowEmptySelection
        aria-label="Table Columns"
        closeOnSelect={false}
        selectedKeys={props.transactionTypeFilter}
        selectionMode="multiple"
        onSelectionChange={props.setTransactionTypeFilter}
        className="text-primary"
      >
        {props.transactionTypeOptions.map((transactionType) => (
          <DropdownItem key={transactionType.uid}>
            <p className="text-primary">{capitalize(transactionType.name)}</p>
          </DropdownItem>
        ))}
      </DropdownMenu>
    </Dropdown>
  );
}
