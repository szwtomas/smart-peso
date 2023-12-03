import { HTTP_STATUS_CONFLICT, HTTP_STATUS_CREATED, HTTP_STATUS_NOT_FOUND, HTTP_STATUS_OK } from "./constants";
import { AuthenticationResponse, SignUpRequestBody } from "./types";

export class Api {
    private host: string;

    constructor(host: string) {
        this.host = host;
    }

    public async logIn(email: string, password: string): Promise<AuthenticationResponse> {
        const response = await fetch(`${this.host}/api/auth/log-in`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ email, password })
        });

        if (response.status === HTTP_STATUS_NOT_FOUND) {
            throw new Error("Invalid email/password combination");
        } else if (response.status !== HTTP_STATUS_OK) {
            throw new Error("Log in failed unexpectedly");
        }

        return await response.json() as AuthenticationResponse;
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

        return await response.json() as AuthenticationResponse;
    }
}
