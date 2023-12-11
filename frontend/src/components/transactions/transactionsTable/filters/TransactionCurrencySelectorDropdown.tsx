import {
  Avatar,
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

export interface TransactionCurrencySelectorDropdownProps {
  transactionCurrencyFilter: Selection;
  setTransactionCurrencyFilter: React.Dispatch<React.SetStateAction<Selection>>;
  transactionCurrencyOptions: TransactionFilterOption[];
}

export function TransactionCurrencySelectorDropdown(
  props: TransactionCurrencySelectorDropdownProps
) {
  return (
    <Dropdown>
      <DropdownTrigger className="hidden md:flex">
        <Button
          endContent={<ChevronDownIcon color="blue" />}
          color="primary"
          variant="ghost"
          size="lg"
          radius="sm"
          className="text-primary"
        >
          <div className="flex flex-row items-center">
            <p className="mr-3 ml-3">Moneda</p>
            <ChevronDownIcon />
          </div>
        </Button>
      </DropdownTrigger>
      <DropdownMenu
        disallowEmptySelection
        aria-label="Table Columns"
        closeOnSelect={false}
        selectionMode="multiple"
        selectedKeys={props.transactionCurrencyFilter}
        onSelectionChange={props.setTransactionCurrencyFilter}
      >
        {props.transactionCurrencyOptions.map((transactionCurrency) => (
          <DropdownItem
            key={transactionCurrency.uid}
            className="capitalize"
            startContent={
              transactionCurrency.name.toLowerCase() === "ars" ? (
                <Avatar
                  alt="Argentina"
                  className="w-6 h-6"
                  src="https://flagcdn.com/ar.svg"
                />
              ) : (
                <Avatar
                  alt="USA"
                  className="w-6 h-6"
                  src="https://flagcdn.com/us.svg"
                />
              )
            }
          >
            <p className="text-primary">
              {capitalize(transactionCurrency.name)}
            </p>
          </DropdownItem>
        ))}
      </DropdownMenu>
    </Dropdown>
  );
}
