import mysql, {Connection, QueryError} from "mysql2";
import {
    connectionOptions as localConnectionOptions,
    prodConnectionOptions
} from "./db/mysqlConnectionOptions";

interface PricesRow {
    date: Date;
    usdOfficial?: number;
    usdMEP?: number;
    usdBlue?: number;
    usdCCL?: number;
}

function getLastDateWithLocalData(connection: Connection, callback: (date: Date | null) => void) {
    const sql: string = "SELECT date FROM currencyPrices ORDER BY date DESC LIMIT 1";
    connection.query(sql, (error: QueryError | null, results) => {
        if (error || !Array.isArray(results)) {
            console.log(`Error in last day query: ${error?.message}`);
            throw new Error("Failed fetching mysql local data");
        }

        const date = results.length > 0 ? (results[0] as any).date : null;
        callback(date);
    });
}

function getProdRowsToInsert(connection: Connection, date: Date | null, callback: (rowsToInsert: PricesRow[]) => void) {
    let sql: string = "SELECT * FROM currencyPrices";
    const queryParams = [];
    if (date != null) {
        sql += " WHERE date >= ?";
        queryParams.push(date);
    }

    connection.query(sql, queryParams, (error, response) => {
        if (error) {
            throw new Error("Error fetching rows to insert: " + error.message);
        }

        callback(response as PricesRow[]);
    });
}

function insertRowsInLocalDB(connection: Connection, rowsToInsert: PricesRow[], callback: (rowsInserted: number) => void) {
    const values = rowsToInsert.map((row) => [
        row.date,
        row.usdOfficial,
        row.usdMEP,
        row.usdBlue,
        row.usdCCL,
    ]);

    const placeholders = rowsToInsert.map(() => '(?, ?, ?, ?, ?)').join(', ');
    const sql: string = "INSERT INTO currencyPrices (date, usdOfficial, usdMEP, usdBlue, usdCCL) VALUES " + placeholders;
    connection.query(sql, values.flat(), (error: QueryError | null) => {
        if (error) {
            throw new Error(`Error inserting rows in local db: ${error.message}`);
        }

        callback(rowsToInsert.length);
    });
}

function getDBConnections(callback: (localConnection: Connection, prodConnection: Connection) => void) {
    const localMySQLConnection: Connection = mysql.createConnection(localConnectionOptions);
    const prodMySQLConnection: Connection = mysql.createConnection(prodConnectionOptions);
    localMySQLConnection.connect((localConnectionError) => {
        prodMySQLConnection.connect((prodConnectionError) => {
            if (localConnectionError || prodConnectionError) {
                throw new Error(
                    "Could not connect to db: Local Error: " +
                    localConnectionError?.message +
                    ", prod error: " +
                    prodConnectionError?.message
                );
            }

            callback(localMySQLConnection, prodMySQLConnection);
        });
    });
}

function runFill() {
    getDBConnections((localConnection, prodConnection) => {
        getLastDateWithLocalData(localConnection, (date: Date | null) => {
            getProdRowsToInsert(prodConnection, date, (rowsToInsert) => {
                if (rowsToInsert.length === 0) {
                    console.log("No rows to insert, program finished");
                    process.exit(0);
                }

                insertRowsInLocalDB(localConnection, rowsToInsert, (insertedRows) => {
                    console.log(`Script finished successfully, inserted ${insertedRows} rows`);
                    process.exit(0);
                });
            });
        });
    });
}

runFill();
