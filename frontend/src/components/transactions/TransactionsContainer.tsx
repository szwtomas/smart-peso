import { useCallback, useContext, useEffect, useMemo, useState } from "react";
import { Selection, SortDescriptor } from "@nextui-org/react";
import {
  columns,
  transactionTypeOptions,
} from "./transactionsTable/columnData";
import { TransactionsTableTopContent } from "./transactionsTable/TransactionsTableTopContent";
import { TransactionTableBottomContent } from "./transactionsTable/TransactionTableBottomContent";
import { TransactionTableCell } from "./transactionsTable/TransactionTableCell";
import { TransactionsTable } from "./transactionsTable/TrasactionsTable";
import {
  CreateTransactionFormData,
  Transaction,
  TransactionContext,
} from "../../context/TransactionContext";
import { toast } from "react-toastify";

export function TransactionContainer() {
  const [needToFetchTransactions, setNeedToFetchTransactions] =
    useState<boolean>(true);
  const [transactionData, setTransactionData] = useState<Transaction[]>([]);
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
  const { getTransactions, addTransaction } = useContext(TransactionContext);

  const pageCount = Math.ceil(transactionData.length / rowsPerPage);
  const hasSearchFilter = Boolean(filterValue);

  useEffect(() => {
    const fetchTransactions = async () => {
      if (!needToFetchTransactions) return;
      try {
        const transactions = await getTransactions();
        setTransactionData(transactions);
        setNeedToFetchTransactions(false);
      } catch (error) {
        if (error instanceof Error) {
          toast.error("Error inesperado obteniendo las transacciones");
        }
        console.error(error);
      }
    };

    fetchTransactions();
  }, [getTransactions, needToFetchTransactions]);

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
  }, [transactionData, filterValue, transactionTypeFilter, hasSearchFilter]);

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
      return (
        <TransactionTableCell transaction={transaction} columnKey={columnKey} />
      );
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
    const onCreateTransaction = async (
      createTransactionData: CreateTransactionFormData
    ) => {
      try {
        await addTransaction(createTransactionData);
        setNeedToFetchTransactions(true);
      } catch (error) {
        console.error(error);
        if (error instanceof Error) {
          toast.error("Error inesperado creando la transacción");
        }
      }
    };

    return (
      <TransactionsTableTopContent
        filterValue={filterValue}
        onClear={onClear}
        onSearchChange={onSearchChange}
        onRowsPerPageChange={onRowsPerPageChange}
        transactionTypeFilter={transactionTypeFilter}
        setTransactionTypeFilter={setTransactionTypeFilter}
        transactionTypeOptions={transactionTypeOptions}
        transactionCount={transactionData.length}
        onCreateTransaction={onCreateTransaction}
      />
    );
  }, [
    filterValue,
    transactionTypeFilter,
    onSearchChange,
    onRowsPerPageChange,
    onClear,
    transactionData.length,
    addTransaction,
  ]);

  const bottomContent = useMemo(() => {
    return (
      <TransactionTableBottomContent
        selectedKeys={selectedKeys}
        page={page}
        pageCount={pageCount}
        setPage={setPage}
        onNextPage={onNextPage}
        onPreviousPage={onPreviousPage}
        filteredItems={filteredItems}
      />
    );
  }, [
    selectedKeys,
    filteredItems,
    page,
    pageCount,
    onNextPage,
    onPreviousPage,
  ]);

  const itemsToShow = useMemo(() => {
    const start = (page - 1) * rowsPerPage;
    const end = start + rowsPerPage;
    return sortedItems.slice(start, end);
  }, [page, rowsPerPage, sortedItems]);

  return (
    <TransactionsTable
      itemsToShow={itemsToShow}
      columns={columns}
      renderCell={renderCell}
      topContent={topContent}
      bottomContent={bottomContent}
      sortDescriptor={sortDescriptor}
      setSortDescriptor={setSortDescriptor}
      setSelectedKeys={setSelectedKeys}
    />
  );
}
