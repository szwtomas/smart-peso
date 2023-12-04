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
        const response = await fetch(`${this.host}/api/auth/log-in`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ email, password })
        });

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
        const response = await fetch(`${this.host}/api/auth/sign-up`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(signUpData)
        });

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
        if (this.accessToken == null) {
            throw new Error("Can't get transactions, Not authenticated");
        }

        const headers = {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${this.accessToken}`
        };
        
        const response = await fetch(`${this.host}/api/transaction`, {
            method: "GET",
            headers
        });

        const jsonResponse = (await response.json()) as unknown as Transaction[];

        return jsonResponse.map((transaction: Transaction) => {
            return {...transaction, date: new Date(transaction.date)};
        });
    }
}
