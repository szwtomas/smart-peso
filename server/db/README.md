# Database Migrations with Flyway
For keeping version control of the Database Changes, this project uses [Flyway](https://flywaydb.org/), which is an Open Source Tool that lets us do Database Migrations.

## Why Migrations
The idea of this is to apply incremental changes on the all environments databases without having to manually execute the queries in the database, avoiding a lot of human errors.
Also, having migrations, it is possible to check how the db schema evolved over time, and also apply changes to older versions without corrupting anything.

## Prerequisites
For performing migrations in both local environment and production, you must have the database connection info. For this, contact the [Project Creator](https://github.com/szwtomas) to ask for the credentials.
Then, you will need to download the Flyway GUI from the [Flyway official page](https://flywaydb.org/) (or you can use the CLI if you prefer).

## Applying Migrations
1. Open the project from the Flyway UI, selecting the `flyway.toml` file in the finder after clicking on "Open Project"
2. Properly connect to the database you want to perform migrations to from the Flyway UI. You can also connect with some DB Client such as the one in IntelliJ to check the current Schema status.
3. Once the database is selected in the UI, just click in the 'Migrate' button and hope for the best :) 

## Adding new Migrations
If you want to add a change to the Database, you can create a new migration clicking 'new migration' in the UI, or adding a new file inside the `/server/db/migrations` directory.
It is important you follow the name convention, adding as version the number that follows the latest migration.

## Future Improvements
In the future, it would be ideal to perform changes in Prod in a CI/CD pipeline, instead of manually from the UI. This would require a LOT of testing, and probably also a test/stage environment. 
