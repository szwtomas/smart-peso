import puppeteer from "puppeteer";
import dotenv from "dotenv";
import { Collector, PriceResult } from "./Collector";
import { USD_OFFICIAL } from "../constants";

dotenv.config();

export class OfficialUSDCollector implements Collector {
    public async collect(): Promise<number> {
        const sources = [this.secondSource, this.firstSource];
        for (const source of sources) {
            const result = await this.tryCollectSource(source);
            if (!result.error) {
                return result.price;
            }
        }

        throw new Error("All sources failed for " + this.currencyName());
    }

    private async tryCollectSource(collectFunction: () => Promise<number>): Promise<PriceResult> {
        try {
            const price = await collectFunction();
            return { price };
        } catch(error) {
            if (error instanceof Error) {
                console.log("Failed fetching official USD: " + error.message);
                return { error: error.message, price: 0 };
            } else {
                return { error: "Unknown error", price: 0 };
            }
        }
    }

    private async firstSource(): Promise<number> {
        const url = process.env.DOLAR_API_HOST || "";
        const response = await fetch(`${url}/oficial`);
        const responseJson = await response.json();
        const sellValue = responseJson?.venta;
        if (!sellValue) {
            throw new Error("Could not get sell value");
        }

        return parseInt(sellValue);
    }

    private async secondSource(): Promise<number> {
        const url = process.env.OFFICIAL_USD_PAGE_URL_SOURCE_1 || "";
        const browser = await puppeteer.launch({ headless: "new" });
        const page = await browser.newPage();
        await page.goto(url);
        await page.waitForSelector(".table.cotizacion"); 
        const targetValue = await page.evaluate(() => {
            const table = document.querySelector(".table.cotizacion");
            const targetRow = table?.querySelectorAll("tr")[1];
            const targetCell = targetRow?.querySelectorAll("td")[2];
            return targetCell?.innerText.trim();
        });

        await browser.close();
        if(!targetValue) {
            throw new Error("Could not get target value");
        }

        return parseInt(targetValue.split(",")[0]);
    }

    public currencyName(): string {
        return USD_OFFICIAL;
    }
}
