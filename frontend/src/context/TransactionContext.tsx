import { createContext } from "react";
import { api } from "../api";

export interface Transaction {
  transactionId: string;
  name: string;
  date: Date;
  value: number;
  type: "income" | "expense";
  description: string;
  category: string;
  currency: "ARS" | "USD";
}

interface TransactionContextProps {
  getTransactions: () => Promise<Transaction[]>;
  addTransaction: (transaction: Transaction) => Promise<void>;
}

const initialTransactionContext: TransactionContextProps = {
  getTransactions: async () => [],
  addTransaction: async () => console.log("Add Transaction"),
};

export const TransactionContext = createContext<TransactionContextProps>(
  initialTransactionContext
);

export interface TransactionProviderProps {
  children: React.ReactNode;
}

export const TransactionProvider: React.FC<TransactionProviderProps> = (
  props: TransactionProviderProps
) => {
  async function getTransactions(): Promise<Transaction[]> {
    return await api.getTransactions();
  }

  async function addTransaction(transaction: Transaction): Promise<void> {
    console.log(transaction);
  }

  return (
    <TransactionContext.Provider
      value={{
        getTransactions,
        addTransaction,
      }}
    >
      {props.children}
    </TransactionContext.Provider>
  );
};
