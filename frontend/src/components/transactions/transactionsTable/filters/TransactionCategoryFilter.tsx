import { Input } from "@nextui-org/react";
import { SearchIcon } from "../../../icons/Icons";

export interface TransactionCategoryFilterInputProps {
  filterValue: string;
  onClear: () => void;
  onSearchChange: (value?: string) => void;
}

export function TransactionCategoryFilter(
  props: TransactionCategoryFilterInputProps
) {
  return (
    <Input
      isClearable
      startContent={<SearchIcon />}
      size="sm"
      radius="sm"
      placeholder="Buscar por categoría..."
      value={props.filterValue}
      onClear={() => props.onClear()}
      onValueChange={props.onSearchChange}
      variant="bordered"
    />
  );
}