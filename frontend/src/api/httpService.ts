export class HttpService {
    private host: string;
    private accessToken?: string;

    constructor(host: string) {
        this.host = host;
    }

    public setAccessToken(token: string) {
        this.accessToken = token;
    }

    public async get(path: string): Promise<Response> {
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

    public async post(path: string, body: unknown): Promise<Response> {
        try {
            return await fetch(`${this.host}${path}`, {
                method: "POST",
                headers: this.getHeaders(),
                body: JSON.stringify(body),
            });
        } catch(err) {
            console.error(err);
            throw new Error("Error inesperado");
        }
    }

    public async put(path: string, body: unknown): Promise<Response> {
        try {
            return await fetch(`${this.host}${path}`, {
                method: "PUT",
                headers: this.getHeaders(),
                body: JSON.stringify(body),
            });
        } catch(err) {
            console.error(err);
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
