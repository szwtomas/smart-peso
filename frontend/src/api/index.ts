import { Api } from "./api";

function getHost(): string {
    if (process.env.VITE_VERCEL_ENVIRONMENT === "prod") {
        const prodHost = process.env.VITE_VERCEL_PROD_HOST as string;
        console.log("Setting host: ", prodHost);
        return prodHost;
    } else {
        const localhost = import.meta.env.VITE_BACKEND_HOST as string;
        console.log("Setting host: ", localhost);
        return localhost;
    }
}

export const api = new Api(getHost());
