# Project-By-Elvara
Application deploy instruction:
1. Download project from this repository
2. Open console panel into **deploy** directory of the project
3. Execute *docker-compose up* command (Docker Desktop installation is required)
4. Wait until info log **INFO:oejs.Server:main: Started** appears
5. To stop application press *Ctrl+C*
6. To destroy application docker containers execute command *docker-compose down*

Use environmental variables into **docker-compose.yaml** file: *ADMIN_ACCOUNT_EMAIL*, *ADMIN_ACCOUNT_PASSWORD*,
*ADMIN_ACCOUNT_NAME*, *ADMIN_ACCOUNT_SURNAME*, *ADMIN_ACCOUNT_PHONE* – to setup admin account email, password,
first name, last name and phone accordingly.

You can deploy application using maven (execute command *mvn jetty:run*, plugin provided), but there are some
prerequisites:
1. MySQL Server must be installed on your device
2. Execute **db-init-script.sql** located in the *deploy -> database* directory on your MySQL Server 
to create database and tables
3. Set maven environmental variables:
   - APP_DATABASE_USERNAME for database user
   - APP_DATABASE_PASSWORD for database user password
   - APP_DATABASE_URL for connection url this MySQL Server (e.g. jdbc:mysql://localhost:3306/supermarket_db)
   - ADMIN_ACCOUNT_EMAIL for admin account email
   - ADMIN_ACCOUNT_PASSWORD for admin account password
   - ADMIN_ACCOUNT_NAME for admin account first name
   - ADMIN_ACCOUNT_SURNAME for admin account last name
   - ADMIN_ACCOUNT_PHONE for admin account phone

Will be grateful for your bug reports