import { Collector } from "./collectors/Collector";
import { OfficialUSDCollector } from "./collectors/OfficialUSDCollector";

export class PricesCollector {
    private collectors: Collector[];

    constructor() {
        this.collectors = [new OfficialUSDCollector()];
    }

    public async run() {
        console.log("Price collector running...");
        for (const collector of this.collectors) {
            console.log(`About to collect price for ${collector.currencyName()}`);
            const price = await collector.collect();
            console.log(`Price for ${collector.currencyName()} is ${price}`);
        }
    }
}
