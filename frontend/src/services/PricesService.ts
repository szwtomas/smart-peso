import { HttpService } from "./HttpService";
import { HTTP_STATUS_OK } from "./httpConstants";

export interface DatePriceSummary {
    date: Date;
    official: number;
    mep: number;
    ccl: number;
    blue: number;
}

export interface UsdPricesSummary {
    today: DatePriceSummary;
    yesterday: DatePriceSummary;
    weekAgo: DatePriceSummary;
    monthAgo: DatePriceSummary;
    yearAgo: DatePriceSummary;
}

export class PricesService {
    private httpService: HttpService;

    constructor(httpService: HttpService) {
        this.httpService = httpService;
    }

    public async getUsdPricesSummary() {
        const response = await this.httpService.get("/api/prices/usd/summary");
        if (response.status !== HTTP_STATUS_OK) {
            console.error(await response.text());
            throw new Error("Error inesperado al obtener los precios");
        }

        const responseJson = await response.json();
        responseJson.today.date = new Date(responseJson.today.date);
        responseJson.yesterday.date = new Date(responseJson.yesterday.date);
        responseJson.weekAgo.date = new Date(responseJson.weekAgo.date);
        responseJson.monthAgo.date = new Date(responseJson.monthAgo.date);
        responseJson.yearAgo.date = new Date(responseJson.yearAgo.date);
        return responseJson as UsdPricesSummary;
    }

    public async getMonthlyPrices() {
        const response = await this.httpService.get("/api/prices/usd/monthly");
        if (response.status !== HTTP_STATUS_OK) {
            console.error(await response.text());
            throw new Error("Error inesperado al obtener los precios");
        }

        const responseJson = await response.json();

        return responseJson;
    }
}