import dotenv from "dotenv";
import cron from "node-cron";
import { PricesCollector } from "./PricesCollector";
import { AppRunner } from "./AppRunner";

dotenv.config();

const runOnce = process.env.RUN_ONCE;

async function collectPrice() {
    const priceCollector = new PricesCollector();
    await new AppRunner(priceCollector).run();
}

if (runOnce) {
    collectPrice().then(() => process.exit(0));
} else {
    cron.schedule("0 * * * *", async () => {
        console.log("Running Price Collector Cron");
        const priceCollector = new PricesCollector();
        const appRunner = new AppRunner(priceCollector);
        appRunner.run();
    });
}
