export interface Transaction {
    id: number;
    name: string;
    date: Date;
    amount: number;
    type: "income" | "expense";
    description: string;
    category: string;
}

export const transactionData: Transaction[] = [
    {
        id: 1,
        name: "Sueldo",
        date: new Date("2023-1-1"),
        amount: 1000,
        type: "income",
        description: "Sueldo de enero",
        category: "Sueldo"
    },
    {
        id: 2,
        name: "Alquiler",
        date: new Date("2023-1-1"),
        amount: 300,
        type: "expense",
        description: "Alquiler de enero",
        category: "Alquiler"
    },
    {
        id: 3,
        name: "Comida",
        date: new Date("2023-1-2"),
        amount: 200,
        type: "expense",
        description: "Compra grande de comida para todo el mes",
        category: "Comida",
    },
    {
        id: 4,
        name: "Boleta de Internet",
        date: new Date("2023-01-03"),
        amount: 100,
        type: "expense",
        description: "Boleta de internet",
        category: "Servicios",
    },
    {
        id: 5,
        name: "Changa",
        date: new Date("2023-1-5"),
        amount: 50,
        type: "income",
        description: "Ayude a X y me pagó 50 pesos",
        category: "other",
    }
]