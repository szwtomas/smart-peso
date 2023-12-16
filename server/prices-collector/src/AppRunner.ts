import { PricesCollector } from "./PricesCollector";

export class AppRunner {
    private pricesCollector: PricesCollector;

    constructor(pricesCollector: PricesCollector) {
        this.pricesCollector = pricesCollector;
    }

    public async run(): Promise<void> {
        const prices = await this.pricesCollector.collect();
        console.log(prices);
    }
}