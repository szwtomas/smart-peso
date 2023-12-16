import { Collector } from "./collectors/Collector";
import { OfficialUSDCollector } from "./collectors/OfficialUSDCollector";
import { PriceCollectorResult } from "./collectors/PriceCollectorResult";
import { BTC, USD_BLUE, USD_CCL, USD_MEP, USD_OFFICIAL } from "./constants";

export class PricesCollector {
    private collectors: Collector[];

    constructor() {
        this.collectors = [new OfficialUSDCollector()];
    }

    public async collect(): Promise<PriceCollectorResult> {
        const prices: Record<string, number> = {};
        console.log("Price collector running...");
        for (const collector of this.collectors) {
            try {
                const price = await collector.collect();
                prices[collector.currencyName()] = price;
            } catch(err) {
                if (err instanceof Error) {
                    console.log("Failed fetching " + collector.currencyName() + ": " + err.message);
                }
            }
            
        }

        return {
            usdOfficial: prices[USD_OFFICIAL],
            usdMep: prices[USD_MEP],
            usdCcl: prices[USD_CCL],
            usdBlue: prices[USD_BLUE],
            btc: prices[BTC],
        };
    }
}
