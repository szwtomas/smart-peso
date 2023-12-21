import { ConnectionOptions } from "mysql2";
import * as dotenv from "dotenv";

dotenv.config();

export const connectionOptions: ConnectionOptions = {
    host: process.env.MYSQL_HOST,
    port: Number(process.env.MYSQL_PORT),
    user: process.env.MYSQL_USER,
    database: process.env.MYSQL_DATABASE,
    password: process.env.MYSQL_PASSWORD,
};

export const prodConnectionOptions: ConnectionOptions = {
    host: process.env.MYSQL_HOST_PROD || "",
    port: Number(process.env.MYSQL_PORT_PROD || "1"),
    user: process.env.MYSQL_USER_PROD || "",
    database: process.env.MYSQL_DATABASE_PROD || "",
    password: process.env.MYSQL_PASSWORD_PROD || "",
};
