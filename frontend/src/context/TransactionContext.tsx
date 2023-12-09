import { createContext } from "react";
import { api } from "../api";

export interface Transaction {
  transactionId: string;
  name: string;
  date: string; // yyyy-mm-dd
  value: number;
  type: "income" | "expense";
  description: string;
  category: string;
  currency: "ARS" | "USD";
}

export interface CreateTransactionFormData {
  transactionName: string;
  transactionType: string;
  currency: string;
  description: string;
  transactionValue: number;
  category: string;
  date: string; // yyyy-mm-dd
}

export interface TransactionDTO {
  name: string;
  type: string;
  currency: string;
  value: number;
  category: string;
  description: string;
  date: string; // yyyy-mm-dd
}

interface TransactionContextProps {
  getTransactions: () => Promise<Transaction[]>;
  addTransaction: (transaction: CreateTransactionFormData) => Promise<void>;
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

  async function addTransaction(
    createTransactionData: CreateTransactionFormData
  ): Promise<void> {
    await api.createTransaction({
      name: createTransactionData.transactionName,
      type: createTransactionData.transactionType,
      currency: createTransactionData.currency,
      value: createTransactionData.transactionValue,
      category: createTransactionData.category,
      description: createTransactionData.description,
      date: createTransactionData.date,
    });
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
