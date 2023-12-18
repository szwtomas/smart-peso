import { USD_BLUE } from "../constants";
import { Collector } from "./Collector";

export class BlueUSDCollector implements Collector {
    public async collect(): Promise<number> {
        const url = process.env.DOLAR_API_HOST || "";
        const response = await fetch(`${url}/blue`);
        const responseJson = await response.json();
        const blueValue = responseJson?.venta;
        if (!blueValue) {
            throw new Error("Could not get blue value");
        }

        return parseInt(blueValue);
    }

    public currencyName(): string {
        return USD_BLUE;
    }
}
