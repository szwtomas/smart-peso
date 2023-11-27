import { Button, Pagination, Selection } from "@nextui-org/react";
import { Transaction } from "../Transaction";
import { Dispatch } from "react";

export interface TransactionTableBottomContentProps {
  filteredItems: Transaction[];
  selectedKeys: Selection;
  page: number;
  pageCount: number;
  setPage: Dispatch<React.SetStateAction<number>>;
  onNextPage: () => void;
  onPreviousPage: () => void;
}

export function TransactionTableBottomContent(
  props: TransactionTableBottomContentProps
) {
  return (
    <div className="py-2 px-2 flex justify-between items-center">
      <span className="w-[30%] text-small text-default-400">
        {props.selectedKeys === "all"
          ? "All items selected"
          : `${props.selectedKeys.size} de ${props.filteredItems.length} seleccionadas`}
      </span>
      <Pagination
        isCompact
        showControls
        showShadow
        color="primary"
        page={props.page}
        total={props.pageCount}
        onChange={props.setPage}
      />
      <div className="hidden sm:flex w-[30%] justify-end gap-2">
        <Button
          isDisabled={props.pageCount === 1}
          size="sm"
          variant="flat"
          onPress={props.onPreviousPage}
        >
          Anterior
        </Button>
        <Button
          isDisabled={props.pageCount === 1}
          size="sm"
          variant="flat"
          onPress={props.onNextPage}
        >
          Siguiente
        </Button>
      </div>
    </div>
  );
}
