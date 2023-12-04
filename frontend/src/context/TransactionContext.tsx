import { createContext, useContext, useEffect, useState } from "react";
import { api } from "../api";
import { AuthContext, User } from "./AuthContext";

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
  transactions: Transaction[];
  addTransaction: (transaction: Transaction) => Promise<void>;
}

const initialTransactionContext: TransactionContextProps = {
  transactions: [],
  addTransaction: async () => console.log("Add Transaction"),
};

export const TransactionContext = createContext<TransactionContextProps>(
  initialTransactionContext
);

export interface TransactionProviderProps {
  children: React.ReactNode;
}

function isUserAuthenticated(user: User | null): boolean {
  return user !== null && user.accessToken !== undefined;
}

export const TransactionProvider: React.FC<TransactionProviderProps> = (
  props: TransactionProviderProps
) => {
  const authContext = useContext(AuthContext);
  const isAuthenticated = isUserAuthenticated(authContext.user);
  const [transactions, setTransactions] = useState<Transaction[]>([]);

  useEffect(() => {
    if (!isAuthenticated) {
      return;
    }

    const fetchTransactions = async () => {
      const transactions = await api.getTransactions();
      setTransactions(transactions);
    };

    fetchTransactions();
  }, [isAuthenticated]);

  const addTransaction = async (transaction: Transaction): Promise<void> => {
    console.log(transaction);
  };

  return (
    <TransactionContext.Provider
      value={{
        transactions: transactions,
        addTransaction,
      }}
    >
      {props.children}
    </TransactionContext.Provider>
  );
};
