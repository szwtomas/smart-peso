# Price Collector

The purpouse of this project is to collect prices from some source and to insert them in the database.
It is a Cronjob, which runs every hour. This allows us to have a history of price.

## Requirements.

This Project uses Node 18.
Also, it inserts the collected prices in the MySQL database, so you should have the credectials. For that, ask the Project Owner for the .env file, which is not pushed into the git repository for security reasons.

## How to run

First, you need to install the dependencies. For that, just execute `npm install` from the project root.

To build and Run the project, just execute with `npm run dev`. You can also compile the code with `npm run build` and then run it from the compiled sources with `npm run start`.
