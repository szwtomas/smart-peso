import { AuthService } from "./AuthService";
import { HttpService } from "./HttpService";
import { TransactionService } from "./TransactionService";

function getHost(): string {
    const localhost = import.meta.env.VITE_BACKEND_HOST;
    if (localhost) {
        return localhost as string;
    } else {
        return "https://smart-peso-production.up.railway.app";
    }
}

const host = getHost();

export const httpService = new HttpService(host);
export const authService = new AuthService(httpService);
export const transactionService = new TransactionService(httpService);
