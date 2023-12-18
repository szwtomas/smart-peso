export interface PriceResult {
    error?: string;
    price: number;
}

export interface Collector {
    collect: () => Promise<number>;
    currencyName: () => string;
}
