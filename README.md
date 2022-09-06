# Project-By-Elvara
## Project structure description
My project is monolithic application based on 3-tier architecture.<br>
Presentation tier is realized using HTML, CSS, Javascript and Thymeleaf technology to simplify data representation to UI.<br>
Application tier is based on layer architecture and is realized using Spring framework. Spring MVC was used to implement presentation layer. Persistence layer is realized with DAO pattern interfaces using JPA and JDBC implementations. Security aspect of the application is implemented with Spring Security.<br>
MySQL Server is used for Data tier.<br>

## Application deploy instruction:
Deploy with **Docker Compose**:
1. Download project from this repository (or copy *docker-compose.yaml* file)
2. Open console panel into **deploy** directory of the project (or directory where *docker-compose.yaml* file was downloaded)
3. Execute *docker-compose up* command (Docker Desktop installation is required)
4. Wait until info log **INFO:oejs.Server:main: Started** appears
5. Access web service via http://localhost:8080/ url (port can be changed, rewrite **ports** parameter of *docker-compose.yaml* file)
6. To stop application press *Ctrl+C*
7. To destroy application docker containers execute command *docker-compose down*

Use environmental variables into **docker-compose.yaml** file: *ADMIN_ACCOUNT_EMAIL*, *ADMIN_ACCOUNT_PASSWORD*, *ADMIN_ACCOUNT_NAME*, *ADMIN_ACCOUNT_SURNAME*, *ADMIN_ACCOUNT_PHONE* – to setup admin account email, password, first name, last name and phone accordingly. Also *SPRING_PROFILES_ACTIVE* environmental variable can be used to choose DAO pattern implementation: *JPA* or *JDBC* (appropriate values for variable, default *JPA*)

You can deploy application using **Maven** (execute command **mvn jetty:run**, plugin provided), but there are some prerequisites:
1. MySQL Server must be installed on your device
2. Execute **db-init-script.sql** located in the *deploy -> database* directory on your MySQL Server to create database and tables
3. Set maven environmental variables:
   - *APP_DATABASE_USERNAME* for database user
   - *APP_DATABASE_PASSWORD* for database user password
   - *APP_DATABASE_URL* for connection url this MySQL Server (e.g. jdbc:mysql://localhost:3306/supermarket_db)
   - *ADMIN_ACCOUNT_EMAIL* for admin account email
   - *ADMIN_ACCOUNT_PASSWORD* for admin account password
   - *ADMIN_ACCOUNT_NAME* for admin account first name
   - *ADMIN_ACCOUNT_SURNAME* for admin account last name
   - *ADMIN_ACCOUNT_PHONE* for admin account phone
   - *SPRING_PROFILES_ACTIVE* for DAO pattern implementation (*JPA/JDBC* values)

Will be grateful for your bug reports