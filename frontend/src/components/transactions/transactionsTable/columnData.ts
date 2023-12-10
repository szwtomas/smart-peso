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
    { name: "VALOR", uid: "value", sortable: true},
    { name: "CATEGORÍA", uid: "category", sortable: true},
    { name: "", uid: "actions", sortable: false},
];  
