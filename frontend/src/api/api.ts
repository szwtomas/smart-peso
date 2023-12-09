import { Transaction, TransactionDTO } from "../context/TransactionContext";
import { HTTP_STATUS_ACCEPTED, HTTP_STATUS_CONFLICT, HTTP_STATUS_CREATED, HTTP_STATUS_NOT_FOUND, HTTP_STATUS_OK } from "./httpConstants";
import { HttpService } from "./httpService";
import { AuthenticationResponse, SignUpRequestBody } from "./types";

export class Api {
    private httpService: HttpService;

    constructor(host: string) {
        this.httpService = new HttpService(host);
    }

    public async logIn(email: string, password: string): Promise<AuthenticationResponse> {
        const response = await this.httpService.post("/api/auth/log-in", { email, password });
        if (response.status === HTTP_STATUS_NOT_FOUND) {
            throw new Error("mail/contraseña invalido/s");
        } else if (response.status !== HTTP_STATUS_OK) {
            throw new Error("Inicio de sesión falló inesperadamente");
        }
        
        const authResponse: AuthenticationResponse = await response.json();
        this.httpService.setAccessToken(authResponse.accessToken);
        return authResponse;
    }

    public async signUp(signUpData: SignUpRequestBody): Promise<AuthenticationResponse> {
        const response = await this.httpService.post("/api/auth/sign-up", signUpData);
        if (response.status === HTTP_STATUS_CONFLICT) {
            console.error(await response.text());
            throw new Error("El usuario ya existe");
        } else if (response.status !== HTTP_STATUS_CREATED) {
            console.error(await response.text());
            throw new Error("La creación del usuario falló inesperadamente");
        }

        const authResponse: AuthenticationResponse = await response.json();
        this.httpService.setAccessToken(authResponse.accessToken);
        return authResponse;
    }

    public async getTransactions(): Promise<Transaction[]> {
        const response = await this.httpService.get("/api/transaction");
        return (await response.json()) as unknown as Transaction[];
    }

    public async createTransaction(transaction: TransactionDTO): Promise<void> {
        const response = await this.httpService.post("/api/transaction", transaction);
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

    public async deleteTransaction(transactionId: string): Promise<void> {
        const response = await this.httpService.delete(`/api/transaction`, { transactionId });
        if (response.status !== HTTP_STATUS_ACCEPTED) {
            console.error(await response.text());
            throw new Error("Error inesperado al eliminar la transacción");
        }
    }
}
