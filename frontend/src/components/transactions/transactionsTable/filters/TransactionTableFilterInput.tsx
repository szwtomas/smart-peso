import { Input } from "@nextui-org/react";
import { SearchIcon } from "../../../icons/Icons";

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
      className="w-[200%]"
      size="sm"
      radius="sm"
      placeholder="Buscar por nombre..."
      value={props.filterValue}
      onClear={() => props.onClear()}
      onValueChange={props.onSearchChange}
      variant="bordered"
      color="primary"
    />
  );
}
