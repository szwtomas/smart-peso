import { useCallback, useContext, useEffect, useMemo, useState } from "react";
import { Selection, SortDescriptor } from "@nextui-org/react";
import { columns } from "./transactionsTable/columnData";
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
import {
  filterTransactions,
  transactionCurrencyOptions,
  transactionTypeOptions,
} from "./transactionsTable/transactionFilters";
import { sortTransactions } from "./transactionsTable/transactionSort";

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
  const [transactionCurrencyFilter, setTransactionCurrencyFilter] =
    useState<Selection>("all");
  const [sortDescriptor, setSortDescriptor] = useState<SortDescriptor>({
    column: "date",
    direction: "descending",
  });
  const {
    getTransactions,
    addTransaction,
    editTransaction,
    deleteTransaction,
  } = useContext(TransactionContext);

  const pageCount = Math.ceil(transactionData.length / rowsPerPage);
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
    return filterTransactions(transactionData, {
      name: filterValue,
      type: transactionTypeFilter,
      currency: transactionCurrencyFilter,
    });
  }, [
    transactionData,
    filterValue,
    transactionTypeFilter,
    transactionCurrencyFilter,
  ]);

  const sortedItems = useMemo(() => {
    return sortTransactions(filteredItems, sortDescriptor);
  }, [sortDescriptor, filteredItems]);

  const renderCell = useCallback(
    (transaction: Transaction, columnKey: React.Key) => {
      const onSaveEdit = async (transaction: Transaction) => {
        try {
          await editTransaction(transaction);
          setNeedToFetchTransactions(true);
          toast.success("Transacción editada!");
        } catch (error) {
          console.error(error);
          if (error instanceof Error) {
            toast.error("Error inesperado editando la transacción");
          }
        }
      };

      const onDelete = async (transaction: Transaction) => {
        try {
          await deleteTransaction(transaction);
          setNeedToFetchTransactions(true);
          toast.success("Transacción eliminada!");
        } catch (error) {
          console.error(error);
          if (error instanceof Error) {
            toast.error("Error inesperado eliminando la transacción");
          }
        }
      };

      return (
        <TransactionTableCell
          transaction={transaction}
          columnKey={columnKey}
          onSaveEditTransaction={onSaveEdit}
          onDeleteTransaction={onDelete}
        />
      );
    },
    [editTransaction, setNeedToFetchTransactions, deleteTransaction]
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
        toast.success("Transacción Creada!");
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
        transactionCurrencyFilter={transactionCurrencyFilter}
        setTransactionCurrencyFilter={setTransactionCurrencyFilter}
        transactionCurrencyOptions={transactionCurrencyOptions}
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
    transactionCurrencyFilter,
    setTransactionCurrencyFilter,
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
    <>
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
    </>
  );
}
