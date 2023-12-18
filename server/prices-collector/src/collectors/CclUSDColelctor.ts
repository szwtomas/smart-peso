import { USD_CCL } from "../constants";
import { Collector } from "./Collector";

export class CclUSDCollector implements Collector {
    public async collect(): Promise<number> {
        const url = process.env.DOLAR_API_HOST || "";
        const response = await fetch(`${url}/contadoconliqui`);
        const responseJson = await response.json();
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
