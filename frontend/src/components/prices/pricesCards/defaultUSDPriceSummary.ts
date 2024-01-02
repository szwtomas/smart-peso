import { UsdPricesSummary } from "../../../services/PricesService";

export const defaultSummary: UsdPricesSummary = {
    today: {
        date: new Date(),
        official: 0,
        mep: 0,
        ccl: 0,
        blue: 0,
    },
    yesterday: {
        date: new Date(),
        official: 0,
        mep: 0,
        ccl: 0,
        blue: 0,
    },
    weekAgo: {
        date: new Date(),
        official: 0,
        mep: 0,
        ccl: 0,
        blue: 0,
    },
    monthAgo: {
        date: new Date(),
        official: 0,
        mep: 0,
        ccl: 0,
        blue: 0,
    },
    yearAgo: {
        date: new Date(),
        official: 0,
        mep: 0,
        ccl: 0,
        blue: 0,
    },
}