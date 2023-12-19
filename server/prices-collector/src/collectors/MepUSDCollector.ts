import { USD_MEP } from "../constants";
import { fetchJson } from "../utils/httpUtils";
import { Collector } from "./Collector";

export class MepUSDCollector implements Collector {
    public async collect(): Promise<number> {
        const url = process.env.DOLAR_API_HOST || "";
        const responseJson = await fetchJson(`${url}/bolsa`);
        const mepValue = responseJson.venta;
        if (!mepValue) {
            throw new Error("Could not get MEP value");
        }

        return parseInt(mepValue);
    }

    public currencyName(): string {
        return USD_MEP;
    }
}
