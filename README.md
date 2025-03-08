# Try Hexagonal
Project that will implement a Tasks list app, in order to put in practice Hexagonal Architecture with a Spring Boot app.

## Run
Before starting the app, you must run:

`docker pull postgres`

`docker run --name my-postgres -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=1234 -d -p 5432:5432 postgres`

Connect to postgres with a DBMS like DBeaver and create the DB **_tasks_**