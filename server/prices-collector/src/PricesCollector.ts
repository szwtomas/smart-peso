import {BlueUSDCollector} from "./collectors/BlueUSDCollector";
import {CclUSDCollector} from "./collectors/CclUSDColelctor";
import {Collector} from "./collectors/Collector";
import {MepUSDCollector} from "./collectors/MepUSDCollector";
import {OfficialUSDCollector} from "./collectors/OfficialUSDCollector";
import {PriceCollectorResult} from "./collectors/PriceCollectorResult";
import {USD_BLUE, USD_CCL, USD_MEP, USD_OFFICIAL} from "./constants";

export class PricesCollector {
    private collectors: Collector[];

    constructor() {
        this.collectors = [
            new OfficialUSDCollector(),
            new BlueUSDCollector(),
            new MepUSDCollector(),
            new CclUSDCollector(),
        ];
    }

    public async collect(): Promise<PriceCollectorResult> {
        const prices: Record<string, number> = {};
        console.log("Price collector running...");
        for (const collector of this.collectors) {
            try {
                prices[collector.currencyName()] = await collector.collect();
            } catch(err) {
                if (err instanceof Error) {
                    console.log(`Failed fetching ${collector.currencyName()}: ${err.message}`);
                }
            }
            
        }

        console.log("Price collector finished");
        return {
            usdOfficial: prices[USD_OFFICIAL],
            usdMEP: prices[USD_MEP],
            usdCCL: prices[USD_CCL],
            usdBlue: prices[USD_BLUE],
        };
    }
}
