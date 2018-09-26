# DREIMT - Drug REpositioning for IMmuno Transcriptomics

## Running the application
The application has been configured to be easily run locally, by just invoking
a Maven command.

To do so, Maven will download (if it is not already) a clean WildFly
distribution to the `target` folder, configure it, start it and deploy the
application on it.

This makes very easy and straightforward to manually test the application.

### Configure a local MySQL
To execute the application you need a MySQL server running in `localhost` and
using the default port (3306).

In this server you have to create a database named `dreimt` accessible for the
`dreimt` user using the `dreimtpass` password.

This can be configured executing the follow SQL sentences in your MySQL:

```SQL
CREATE DATABASE dreimt;
GRANT ALL ON dreimt.* TO dreimt@localhost IDENTIFIED BY 'dreimtpass';
FLUSH PRIVILEGES;
```

Of course, this configuration can be changed in the POM file.

### Building the application
The application can be built with the following Maven command:

```
mvn clean install
```

This will build the application launching the tests on a **Wildfly 10.1.0**
server.

### Starting the application
The application can be started with the following Maven command:

```
mvn package wildfly:start wildfly:deploy-only -P wildfly-mysql-run
```

This will start a **WildFly 10.1.0**.

### Redeploying the application
Once it is running, the application can be re-deployed with the following Maven
command:

```
mvn package wildfly:deploy-only -P wildfly-mysql-run
```

### Stopping the application
The application can be stopped with the following Maven command:

```
mvn wildfly:shutdown
```

### REST API documentation
The REST API is documented using the [Swagger](https://swagger.io/) framework.
It can be browsed using the [Swagger UI](http://petstore.swagger.io/)
application to access the following URL:

```
http://localhost:8080/dreimt-backend/rest/api/swagger.json
```