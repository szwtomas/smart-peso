import { Input } from "@nextui-org/react";
import { SearchIcon } from "../../Icons";

export interface TransactionTableFilterInputProps {
  filterValue: string;
  onClear: () => void;
  onSearchChange: (value?: string) => void;
}

export function TransactionTableFilterInput(
  props: TransactionTableFilterInputProps
) {
  return (
    <Input
      isClearable
      startContent={<SearchIcon />}
      className="w-full"
      size="sm"
      radius="sm"
      placeholder="Buscar por nombre..."
      value={props.filterValue}
      onClear={() => props.onClear()}
      onValueChange={props.onSearchChange}
    />
  );
}
