import { Api } from "./api";

function getHost(): string {
    const localhost = import.meta.env.VITE_BACKEND_HOST;
    if (localhost) {
        return localhost as string;
    } else {
        return "https://smart-peso-production.up.railway.app";
    }
}

const host = getHost();
console.log("Setting backend host: " + host);

export const api = new Api(host);
