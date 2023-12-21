# Price Collector

The purpouse of this project is to collect prices from some source and to insert them in the database.
It is a Cronjob, which runs every hour. This allows us to have a history of price.

## Requirements.

This Project uses Node 18 and Typescript.
Also, it inserts the collected prices in the MySQL database, so you should have the credectials. For that, ask the Project Owner for the .env file, which is not pushed into the git repository for security reasons.

You can also manually create the `.env` file, with the following structure:

```
RUN_ONCE=true/false
OFFICIAL_USD_PAGE_URL_SOURCE_1=************
DOLAR_API_HOST=************
MYSQL_HOST=************
MYSQL_PORT=************
MYSQL_USER=************
MYSQL_PASSWORD=************
MYSQL_DATABASE=************
```

## How to run

First, you need to install the dependencies. For that, just execute `npm install` from the project root.

To build and Run the project, just execute with `npm run dev`. You can also compile the code with `npm run build` and then run it from the compiled sources with `npm run start`.

If you run the code with `npm run dev`, it will run the code once, noe activating the cronjob. This is useful for testing purposes.

## How does it work

First, `price-collector`fetches the USD currency rates into different sources, having a fallback source if it fails for some reason. Once it has the rates, it inserts them into the mysql database, with today's date in Argentina timezome.

## Local Filler Script

This project also contains a npm script `fill-local`, which you can run with `npm run fill-local`, that fills your local MySQL with production data regarding currency prices.
This exists because, since prices are filled with a cronjob, it's hard to keep the local DB updated and quite tedious to add the values manually, so this fills it with the real prices.
You will need the `.env` file with the required environment variables to use this scripts, you can ask the project owner for that file since it is not pushed to the repository.
