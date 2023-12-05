import { Transaction } from "../context/TransactionContext";
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
            throw new Error("Invalid email/password");
        } else if (response.status !== HTTP_STATUS_OK) {
            throw new Error("Log in failed unexpectedly");
        }
        
        const authResponse: AuthenticationResponse = await response.json();
        this.accessToken = authResponse.accessToken;
        return authResponse;
    }

    public async signUp(signUpData: SignUpRequestBody): Promise<AuthenticationResponse> {
        const response = await this.post("/api/auth/sign-up", signUpData);

        if (response.status === HTTP_STATUS_CONFLICT) {
            throw new Error("User already exists");
        } else if (response.status !== HTTP_STATUS_CREATED) {
            throw new Error("Sign up failed unexpectedly");
        }

        const authResponse: AuthenticationResponse = await response.json();
        this.accessToken = authResponse.accessToken;
        return authResponse;
    }

    public async getTransactions(): Promise<Transaction[]> {
        const response = await this.get("/api/transaction");
        const jsonResponse = (await response.json()) as unknown as Transaction[];
        return jsonResponse.map((transaction: Transaction) => {
            return {...transaction, date: new Date(transaction.date)};
        });
    }

    private async get(path: string): Promise<Response> {
        try {
            return await fetch(`${this.host}${path}`, {
                method: "GET",
                headers: this.getHeaders(),
            });
        } catch(err) {
            console.error(err);
            throw new Error("Unexpected error");
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
            throw new Error("Unexpected error");
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
