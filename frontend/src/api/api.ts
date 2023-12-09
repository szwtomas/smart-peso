import { Transaction, TransactionDTO } from "../context/TransactionContext";
import { HTTP_STATUS_CONFLICT, HTTP_STATUS_CREATED, HTTP_STATUS_NOT_FOUND, HTTP_STATUS_OK } from "./constants";
import { AuthenticationResponse, SignUpRequestBody } from "./types";

export class Api {
    private host: string;
    private accessToken: string | null = null;

    constructor(host: string) {
        this.host = host;
        this.accessToken = null;
    }

    public async logIn(email: string, password: string): Promise<AuthenticationResponse> {
        const response = await this.post("/api/auth/log-in", { email, password });

        if (response.status === HTTP_STATUS_NOT_FOUND) {
            throw new Error("mail/contraseña invalido/s");
        } else if (response.status !== HTTP_STATUS_OK) {
            throw new Error("Inicio de sesión falló inesperadamente");
        }
        
        const authResponse: AuthenticationResponse = await response.json();
        this.accessToken = authResponse.accessToken;
        return authResponse;
    }

    public async signUp(signUpData: SignUpRequestBody): Promise<AuthenticationResponse> {
        const response = await this.post("/api/auth/sign-up", signUpData);

        if (response.status === HTTP_STATUS_CONFLICT) {
            throw new Error("El usuario ya existe");
        } else if (response.status !== HTTP_STATUS_CREATED) {
            throw new Error("La creación del usuario falló inesperadamente");
        }

        const authResponse: AuthenticationResponse = await response.json();
        this.accessToken = authResponse.accessToken;
        return authResponse;
    }

    public async getTransactions(): Promise<Transaction[]> {
        const response = await this.get("/api/transaction");
        return (await response.json()) as unknown as Transaction[];
    }

    public async createTransaction(transaction: TransactionDTO): Promise<void> {
        const response = await this.post("/api/transaction", transaction);
        if (response.status !== HTTP_STATUS_CREATED) {
            throw new Error("Error inesperado al crear la transacción");
        }
    }

    private async get(path: string): Promise<Response> {
        try {
            return await fetch(`${this.host}${path}`, {
                method: "GET",
                headers: this.getHeaders(),
            });
        } catch(err) {
            console.error(err);
            throw new Error("Error inesperado");
        }
    }

    private async post(path: string, body: unknown): Promise<Response> {
        try {
            return await fetch(`${this.host}${path}`, {
                method: "POST",
                headers: this.getHeaders(),
                body: JSON.stringify(body),
            });
        } catch(err) {
            console.log(err);
            throw new Error("Error inesperado");
        }
    }

    private getHeaders(): Headers {
        const headers = new Headers();
        headers.append("Content-Type", "application/json");
        if (this.accessToken != null) {
            headers.append("Authorization", `Bearer ${this.accessToken}`);
        }
        return headers;
    }
}
