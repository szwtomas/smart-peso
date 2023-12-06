import { Api } from "./api";

export const api = new Api(import.meta.env.VITE_BACKEND_HOST as string);
