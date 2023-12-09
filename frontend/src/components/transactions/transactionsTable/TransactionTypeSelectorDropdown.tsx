import {
  Button,
  Dropdown,
  DropdownItem,
  DropdownMenu,
  DropdownTrigger,
  Selection,
} from "@nextui-org/react";
import { ChevronDownIcon } from "../../Icons";
import { capitalize } from "../../../utils/utils";
import { TransactionTypeOption } from "./columnData";

export interface TransactionTypeSelectorDropdownProps {
  transactionTypeFilter: Selection;
  setTransactionTypeFilter: React.Dispatch<React.SetStateAction<Selection>>;
  transactionTypeOptions: TransactionTypeOption[];
}

export function TransactioTypeSelectorDropdown(
  props: TransactionTypeSelectorDropdownProps
) {
  return (
    <Dropdown>
      <DropdownTrigger className="hidden sm:flex">
        <Button
          endContent={<ChevronDownIcon />}
          variant="flat"
          size="lg"
          radius="sm"
        >
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
  );
}
