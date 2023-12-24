import * as fs from "fs";
import mysql, {Connection} from "mysql2";
import { connectionOptions } from "./db/mysqlConnectionOptions";

function fillHistoricPrices(connection: Connection) {

    const csvPath = "./data/prices.csv";
    const csvData = fs.readFileSync(csvPath, "utf-8");
    const rows = csvData.trim().split('\n').slice(1);
    for (const row of rows) {
        const [date, official, mep, ccl, blue] = row.split(',');

        // Format date from dd/mm/yyyy to yyyy-mm-dd for MySQL
        const formattedDate = date.split('/').reverse().join('-');
        const sql = "INSERT INTO currencyPrices (date, usdOfficial, usdMEP, usdCCL, usdBlue) VALUES (?, ?, ?, ?, ?)";
        const params = [formattedDate, parseFloat(official), parseFloat(mep), parseFloat(ccl), parseFloat(blue)];
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
    });
}

main();
