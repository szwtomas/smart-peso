import { HTTP_STATUS_ACCEPTED, HTTP_STATUS_CREATED, HTTP_STATUS_OK } from "./httpConstants";
import { HttpService } from "./HttpService";

export interface Transaction {
  transactionId: number;
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

export class TransactionService {
    private httpService: HttpService;

    constructor(httpService: HttpService) {
        this.httpService = httpService;
    }

    public async getTransactions(): Promise<Transaction[]> {
        const response = await this.httpService.get("/api/transaction");
        if (response.status !== HTTP_STATUS_OK) {
            console.error(await response.text());
            throw new Error("Error inesperado al obtener las transacciones");
        }

        return (await response.json()) as unknown as Transaction[];
    }

    public async addTransaction(createTransactionData: CreateTransactionFormData) {
        const transactionDTO: TransactionDTO = {
            name: createTransactionData.transactionName,
            type: createTransactionData.transactionType,
            currency: createTransactionData.currency,
            value: createTransactionData.transactionValue,
            category: createTransactionData.category,
            description: createTransactionData.description,
            date: createTransactionData.date,
        };

        const response = await this.httpService.post("/api/transaction", transactionDTO);
        if (response.status !== HTTP_STATUS_CREATED) {
            console.error(await response.text());
            throw new Error("Error inesperado al crear la transacción");
        }
    }

    public async updateTransaction(transaction: Transaction): Promise<void> {
        const response = await this.httpService.put(`/api/transaction`, transaction);
        if (response.status !== HTTP_STATUS_OK) {
            console.error(await response.text());
            throw new Error("Error inesperado al actualizar la transacción");
        }
    }

    public async deleteTransaction(transactionId: number): Promise<void> {
        const response = await this.httpService.delete(`/api/transaction`, { transactionId });
        if (response.status !== HTTP_STATUS_ACCEPTED) {
            console.error(await response.text());
            throw new Error("Error inesperado al eliminar la transacción");
        }
    }
}
