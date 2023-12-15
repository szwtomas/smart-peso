export interface Collector {
    collect: () => Promise<number>;
    currencyName: () => string;
}