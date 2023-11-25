import { useCallback, useMemo, useState } from "react";
import { Transaction, transactionData } from "../Transaction";
import {
  Button,
  Table,
  TableBody,
  TableCell,
  TableColumn,
  TableHeader,
  TableRow,
  Selection,
  Pagination,
  Dropdown,
  DropdownTrigger,
  Input,
  DropdownMenu,
  DropdownItem,
  SortDescriptor,
} from "@nextui-org/react";
import { columns, transactionTypeOptions } from "./columnData";
import { capitalize } from "../../../utils/utils";
import { ChevronDownIcon, PlusIcon, SearchIcon } from "./Icons";

function dateToString(date: Date): string {
  return `${date.getDate()}/${date.getMonth() + 1}/${date.getFullYear()}`;
}

export function TransactionContainer() {
  const [page, setPage] = useState(1);
  const [filterValue, setFilterValue] = useState("");
  const [selectedKeys, setSelectedKeys] = useState<Selection>(new Set());
  const [rowsPerPage, setRowsPerPage] = useState(5);
  const [transactionTypeFilter, setTransactionTypeFilter] =
    useState<Selection>("all");
  const [sortDescriptor, setSortDescriptor] = useState<SortDescriptor>({
    column: "date",
    direction: "descending",
  });
  const pageCount = Math.ceil(transactionData.length / rowsPerPage);
  const hasSearchFilter = Boolean(filterValue);

  const filteredItems = useMemo(() => {
    let filteredTransactions = [...transactionData];

    if (hasSearchFilter) {
      filteredTransactions = filteredTransactions.filter(
        (transaction: Transaction) =>
          transaction.name.toLowerCase().includes(filterValue.toLowerCase())
      );
    }
    if (
      transactionTypeFilter !== "all" &&
      Array.from(transactionTypeFilter).length !== transactionTypeOptions.length
    ) {
      filteredTransactions = filteredTransactions.filter(
        (transaction: Transaction) =>
          Array.from(transactionTypeFilter).includes(transaction.type)
      );
    }

    return filteredTransactions;
  }, [filterValue, transactionTypeFilter, hasSearchFilter]);

  const sortedItems = useMemo(() => {
    return [...filteredItems].sort((a: Transaction, b: Transaction) => {
      const first = a[sortDescriptor.column as keyof Transaction] as number;
      const second = b[sortDescriptor.column as keyof Transaction] as number;
      const cmp = first < second ? -1 : first > second ? 1 : 0;

      return sortDescriptor.direction === "descending" ? -cmp : cmp;
    });
  }, [sortDescriptor, filteredItems]);

  const renderCell = useCallback(
    (transaction: Transaction, columnKey: React.Key) => {
      const cellValue = transaction[columnKey as keyof Transaction];
      switch (columnKey) {
        case "id":
          return <p>{cellValue as string}</p>;
        case "name":
          return <p>{cellValue as string}</p>;
        case "type":
          return (
            <p
              className={
                transaction.type === "income"
                  ? "text-green-600"
                  : "text-red-600"
              }
            >
              {cellValue === "income" ? "Ingreso" : "Gasto"}
            </p>
          );
        case "date":
          return <p>{dateToString(cellValue as Date)}</p>;
        case "amount":
          return (
            <p
              className={
                transaction.type === "income"
                  ? "text-green-600"
                  : "text-red-600"
              }
            >
              ${cellValue as string}
            </p>
          );
        case "category":
          return <p>{cellValue as string}</p>;
        case "actions":
          return (
            <div>
              <Button size="sm" variant="light">
                <p className="text-xl text-primary">
                  <b>+</b>
                </p>
              </Button>
            </div>
          );
        default:
          return <p className="text-red-600">Error</p>;
      }
    },

    []
  );
  const onNextPage = useCallback(() => {
    if (page < pageCount) {
      setPage(page + 1);
    }
  }, [page, pageCount]);

  const onPreviousPage = useCallback(() => {
    if (page > 1) {
      setPage(page - 1);
    }
  }, [page]);

  const onRowsPerPageChange = useCallback(
    (e: React.ChangeEvent<HTMLSelectElement>) => {
      setRowsPerPage(Number(e.target.value));
      setPage(1);
    },
    []
  );

  const onSearchChange = useCallback((value?: string) => {
    if (value) {
      setFilterValue(value);
      setPage(1);
    } else {
      setFilterValue("");
    }
  }, []);

  const onClear = useCallback(() => {
    setFilterValue("");
    setPage(1);
  }, []);

  const topContent = useMemo(() => {
    return (
      <div className="flex flex-col gap-4">
        <div className="flex justify-between gap-3 items-end">
          <Input
            isClearable
            startContent={<SearchIcon />}
            className="w-full"
            size="sm"
            placeholder="Buscar por nombre..."
            value={filterValue}
            onClear={() => onClear()}
            onValueChange={onSearchChange}
          />
          <div className="flex gap-3">
            <Dropdown>
              <DropdownTrigger className="hidden sm:flex">
                <Button
                  endContent={<ChevronDownIcon />}
                  variant="flat"
                  size="lg"
                >
                  Tipo
                </Button>
              </DropdownTrigger>
              <DropdownMenu
                disallowEmptySelection
                aria-label="Table Columns"
                closeOnSelect={false}
                selectedKeys={transactionTypeFilter}
                selectionMode="multiple"
                onSelectionChange={setTransactionTypeFilter}
              >
                {transactionTypeOptions.map((transactionType) => (
                  <DropdownItem
                    key={transactionType.uid}
                    className="capitalize"
                  >
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
            Total {transactionData.length} transacciones
          </span>
          <label className="flex items-center text-default-400 text-small">
            Rows per page:
            <select
              className="bg-transparent outline-none text-default-400 text-small"
              onChange={onRowsPerPageChange}
            >
              <option value="5">5</option>
              <option value="10">10</option>
              <option value="15">15</option>
            </select>
          </label>
        </div>
      </div>
    );
  }, [
    filterValue,
    transactionTypeFilter,
    onSearchChange,
    onRowsPerPageChange,
    onClear,
  ]);

  const bottomContent = useMemo(() => {
    return (
      <div className="py-2 px-2 flex justify-between items-center">
        <span className="w-[30%] text-small text-default-400">
          {selectedKeys === "all"
            ? "All items selected"
            : `${selectedKeys.size} de ${filteredItems.length} seleccionadas`}
        </span>
        <Pagination
          isCompact
          showControls
          showShadow
          color="primary"
          page={page}
          total={pageCount}
          onChange={setPage}
        />
        <div className="hidden sm:flex w-[30%] justify-end gap-2">
          <Button
            isDisabled={pageCount === 1}
            size="sm"
            variant="flat"
            onPress={onPreviousPage}
          >
            Previous
          </Button>
          <Button
            isDisabled={pageCount === 1}
            size="sm"
            variant="flat"
            onPress={onNextPage}
          >
            Next
          </Button>
        </div>
      </div>
    );
  }, [
    selectedKeys,
    filteredItems.length,
    page,
    pageCount,
    onNextPage,
    onPreviousPage,
  ]);

  return (
    <>
      <Table
        aria-label="users-transactions"
        isHeaderSticky
        classNames={{
          wrapper: "max-h-[382px]",
        }}
        selectionMode="multiple"
        onSelectionChange={setSelectedKeys}
        topContent={topContent}
        topContentPlacement="outside"
        bottomContent={bottomContent}
        bottomContentPlacement="outside"
        sortDescriptor={sortDescriptor}
        onSortChange={setSortDescriptor}
      >
        <TableHeader columns={columns}>
          {columns.map((c) => {
            return (
              <TableColumn key={c.uid} allowsSorting={c.sortable}>
                {c.name}
              </TableColumn>
            );
          })}
        </TableHeader>
        <TableBody emptyContent={"No hay Transacciones"} items={sortedItems}>
          {(t) => {
            return (
              <TableRow key={t.id}>
                {(columnKey) => (
                  <TableCell>{renderCell(t, columnKey)}</TableCell>
                )}
              </TableRow>
            );
          }}
        </TableBody>
      </Table>
    </>
  );
}
