import { USD_CCL } from "../constants";
import { fetchJson } from "../utils/httpUtils";
import { Collector } from "./Collector";

export class CclUSDCollector implements Collector {
    public async collect(): Promise<number> {
        const url = process.env.DOLAR_API_HOST || "";
        const responseJson = await fetchJson(`${url}/contadoconliqui`);
        const cclValue = responseJson?.venta;
        if (!cclValue) {
            throw new Error("Could not get ccl value");
        }

        return parseInt(cclValue);
    }

    public currencyName(): string {
        return USD_CCL;
    }
}
