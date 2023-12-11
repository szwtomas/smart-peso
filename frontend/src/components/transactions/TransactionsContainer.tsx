import { useCallback, useEffect, useMemo, useState } from "react";
import { Selection, SortDescriptor } from "@nextui-org/react";
import { columns } from "./transactionsTable/columnData";
import { TransactionsTableTopContent } from "./transactionsTable/TransactionsTableTopContent";
import { TransactionTableBottomContent } from "./transactionsTable/TransactionTableBottomContent";
import { TransactionTableCell } from "./transactionsTable/TransactionTableCell";
import { TransactionsTable } from "./transactionsTable/TrasactionsTable";
import { toast } from "react-toastify";
import {
  filterTransactions,
  transactionCurrencyOptions,
  transactionTypeOptions,
} from "./transactionsTable/filters/transactionFilters";
import { sortTransactions } from "./transactionsTable/transactionSort";
import {
  CreateTransactionFormData,
  Transaction,
} from "../../services/TransactionService";
import { transactionService } from "../../services";

export function TransactionContainer() {
  const [needToFetchTransactions, setNeedToFetchTransactions] =
    useState<boolean>(true);
  const [transactionData, setTransactionData] = useState<Transaction[]>([]);
  const [page, setPage] = useState(1);
  const [nameFilterValue, setNameFilterValue] = useState("");
  const [categoryFilterValue, setCategoryFilterValue] = useState("");
  const [selectedKeys, setSelectedKeys] = useState<Selection>(new Set());
  const [rowsPerPage, setRowsPerPage] = useState(10);
  const [transactionTypeFilter, setTransactionTypeFilter] =
    useState<Selection>("all");
  const [transactionCurrencyFilter, setTransactionCurrencyFilter] =
    useState<Selection>("all");
  const [sortDescriptor, setSortDescriptor] = useState<SortDescriptor>({
    column: "date",
    direction: "descending",
  });

  const pageCount = Math.ceil(transactionData.length / rowsPerPage);
  useEffect(() => {
    const fetchTransactions = async () => {
      if (!needToFetchTransactions) return;
      try {
        const transactions = await transactionService.getTransactions();
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
  }, [needToFetchTransactions]);

  const filteredItems = useMemo(() => {
    return filterTransactions(transactionData, {
      name: nameFilterValue,
      category: categoryFilterValue,
      type: transactionTypeFilter,
      currency: transactionCurrencyFilter,
    });
  }, [
    transactionData,
    nameFilterValue,
    categoryFilterValue,
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
          await transactionService.updateTransaction(transaction);
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
          await transactionService.deleteTransaction(transaction.transactionId);
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
    [setNeedToFetchTransactions]
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

  const onNameSearchChange = useCallback((value?: string) => {
    if (value) {
      setNameFilterValue(value);
      setPage(1);
    } else {
      setNameFilterValue("");
    }
  }, []);

  const onCategorySearchChange = useCallback((value?: string) => {
    if (value) {
      setCategoryFilterValue(value);
      setPage(1);
    } else {
      setCategoryFilterValue("");
    }
  }, []);

  const onNameFilterClear = useCallback(() => {
    setNameFilterValue("");
    setPage(1);
  }, []);

  const onCategoryFilterClear = useCallback(() => {
    setCategoryFilterValue("");
    setPage(1);
  }, []);

  const topContent = useMemo(() => {
    const onCreateTransaction = async (
      createTransactionData: CreateTransactionFormData
    ) => {
      try {
        await transactionService.addTransaction(createTransactionData);
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
        nameFilterValue={nameFilterValue}
        onNameFilterClear={onNameFilterClear}
        onNameSearchChange={onNameSearchChange}
        categoryFilterValue={categoryFilterValue}
        onCategoryFilterClear={onCategoryFilterClear}
        onCategorySearchChange={onCategorySearchChange}
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
    nameFilterValue,
    transactionTypeFilter,
    onNameSearchChange,
    onRowsPerPageChange,
    onNameFilterClear,
    transactionData.length,
    transactionCurrencyFilter,
    setTransactionCurrencyFilter,
    categoryFilterValue,
    onCategorySearchChange,
    onCategoryFilterClear,
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
