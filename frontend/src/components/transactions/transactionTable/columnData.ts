export interface Column {
    name: string;
    uid: string;
    sortable: boolean;
}

export const columns: Column[] = [
    { name: "NOMBRE", uid: "name", sortable: true},
    { name: "FECHA", uid: "date", sortable: true},
    { name: "TIPO", uid: "type", sortable: true},
    { name: "MONEDA", uid: "currency", sortable: true },
    { name: "VALOR", uid: "amount", sortable: true},
    { name: "CATEGORÍA", uid: "category", sortable: true},
    { name: "VER MÁS", uid: "actions", sortable: false},
];

export const transactionTypeOptions = [
  {name: "Ingreso", uid: "income"},
  {name: "Gasto", uid: "expense"},
];