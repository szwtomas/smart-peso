import puppeteer from "puppeteer";
import dotenv from "dotenv";
import { Collector } from "./Collector";

dotenv.config();

export class OfficialUSDCollector implements Collector {
    private url: string;

    constructor() {
        this.url = process.env.OFFICIAL_USD_PAGE_URL || "";
    }

    public async collect(): Promise<number> {
        const browser = await puppeteer.launch({ headless: "new" });
        const page = await browser.newPage();
        console.log("Page is " + this.url);
        await page.goto(this.url);
        
        await page.waitForSelector('.table.cotizacion'); 

        const targetValue = await page.evaluate(() => {
            const table = document.querySelector(".table.cotizacion");
            const targetRow = table?.querySelectorAll("tr")[1];
            const targetCell = targetRow?.querySelectorAll("td")[2];
            return targetCell?.innerText.trim();
        });

        if(!targetValue) {
            throw new Error('Target value not found');
        }

        const price: number = parseInt(targetValue.split(",")[0]);
        
        return price;
    }

    public currencyName(): string {
        return 'official-usd';
    }
}
