import * as fs from "fs";
import mysql, {Connection} from "mysql2";
import { connectionOptions } from "./db/mysqlConnectionOptions";

const HISTORICAL_PRICES_CSV_PATH: string = "./data/prices.csv";

function formatDateToMySQLFormat(date: string): string {
    return date.split('/').reverse().join('-');
}

function getRowsFromRawCSVContent(csvContent: string): string[] {
    return csvContent
        .trim()
        .split('\n')
        .slice(1);
}

function fillHistoricPrices(connection: Connection) {
    const csvContent = fs.readFileSync(HISTORICAL_PRICES_CSV_PATH, "utf-8");
    const rows = getRowsFromRawCSVContent(csvContent);
    for (const row of rows) {
        const [date, official, mep, ccl, blue] = row.split(',');
        const formattedDate = formatDateToMySQLFormat(date);
        const sql: string = "INSERT INTO currencyPrices (date, usdOfficial, usdMEP, usdCCL, usdBlue) VALUES (?, ?, ?, ?, ?)";
        const params = [
            formattedDate,
            parseFloat(official),
            parseFloat(mep),
            parseFloat(ccl),
            parseFloat(blue)
        ];
        connection.query(sql, params, (error, result) => {
            if (error) {
                console.log("Failed inserting row");
            } else {
                console.log("Inserted row");
                console.log(result);
            }
        });
    }
}

function main() {
    const connection: Connection = mysql.createConnection(connectionOptions);
    connection.connect((error) => {
       if (error) {
           console.log(`Failed connecting to db: ${error.message}`);
           process.exit(1);
       }

       fillHistoricPrices(connection);
       process.exit(0);
    });
}

main();
