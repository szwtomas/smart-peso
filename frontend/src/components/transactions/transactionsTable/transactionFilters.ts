import { Selection } from "@nextui-org/react";
import { Transaction } from "../../../services/TransactionService";

export interface TransactionFilterOption {
  name: string;
  uid: string;
}

export const transactionCurrencyOptions: TransactionFilterOption[] = [
  { name: "ARS", uid: "ars"},
  { name: "USD", uid: "usd"},
]

export const transactionTypeOptions: TransactionFilterOption[] = [
  {name: "Ingreso", uid: "income"},
  {name: "Gasto", uid: "expense"},
];

export interface TransactionFilters {
    name: string;
    currency: Selection;
    type: Selection;
}

export function filterTransactions(transactions: Transaction[], filters: TransactionFilters): Transaction[] {
    let filteredTransactions = [...transactions];
    const nameFilter = filters.name.toLowerCase();
    const currencyFilter = filters.currency;
    const typeFilter = filters.type;

    if (nameFilter !== "") {
        filteredTransactions = filteredTransactions.filter((transaction) => transaction.name.toLowerCase().includes(nameFilter));
    }

    if (typeFilter !== "all" && Array.from(typeFilter).length !== transactionTypeOptions.length) {
        filteredTransactions = filteredTransactions.filter((transaction) => Array.from(typeFilter).includes(transaction.type));
    }

    if (currencyFilter !== "all" && Array.from(currencyFilter).length !== transactionCurrencyOptions.length) {
        filteredTransactions = filteredTransactions.filter((transaction) => Array.from(currencyFilter).includes(transaction.currency.toLocaleLowerCase()));
    }
    
    return filteredTransactions;
}