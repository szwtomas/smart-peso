import dotenv from "dotenv";
import cron from "node-cron";
import { PricesCollector } from "./PricesCollector";
import { insertPricesInDB } from "./priceDbInserter";

dotenv.config();

const runOnce = process.env.RUN_ONCE;

async function run() {
    const prices = await new PricesCollector().collect();
    insertPricesInDB(prices);
}

console.log("App Started");

if (runOnce) {
    run();
} else {
    cron.schedule("0 * * * *", async () => {
        console.log("Running Price Collector Cron");
        run();
    });
}
