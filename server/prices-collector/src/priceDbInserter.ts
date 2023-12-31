import mysql, { ResultSetHeader } from "mysql2";
import { PriceCollectorResult } from "./collectors/PriceCollectorResult";
import { connectionOptions } from "./db/mysqlConnectionOptions";

export function insertPricesInDB(prices: PriceCollectorResult) {
    const connection = mysql.createConnection(connectionOptions);
    connection.connect((connectionError) => {
        if (connectionError) throw connectionError;
        console.log("Connected to database");
        const sql = "INSERT INTO currencyPrices SET ?";
        const rowData = {
            usdOfficial: prices.usdOfficial,
            usdBlue: prices.usdBlue,
            usdCCL: prices.usdCCL,
            usdMEP: prices.usdMEP,
        };
        
        connection.query(sql, rowData, (queryError, results: ResultSetHeader) => {
            connection.end();
            if (queryError || !results || results.affectedRows !== 1) {
                console.error("Error inserting price: " + queryError?.message);
                return;
            }

            console.log(`Inserted ${results.affectedRows} row(s) in database`);
            console.log("Prices inserted: " + JSON.stringify(rowData));
        });
    });
}
