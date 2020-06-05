# DREIMT [![license](https://img.shields.io/github/license/sing-group/dreimt-backend)](https://github.com/sing-group/dreimt-backend) [![release](https://img.shields.io/github/release/sing-group/dreimt-backend.svg)](https://github.com/sing-group/dreimt-backend/releases)

> DREIMT - A tool for immune modulation drug prioritization (http://www.dreimt.org)

<p align="center">
	<img src="additional-material/dreimt-logo.png" alt="DREIMT logo"></img>
</p>

**DREIMT** is a bioinformatics tool for hypothesis generation and prioritization of drugs capable of modulating immune cell activity from transcriptomics data.

DREIMT integrates 4,694 drug profiles from The Library of Network-Based Cellular Signatures (LINCS) L1000 data set and 2,687 manually curated immune gene expression signatures from multiple resources to generate a drug-immune signature association database.

DREIMT can also prioritize drug associations from user-provided immune signatures. 

## Development 

### Running the application
The application has been configured to be easily run locally, by just invoking a Maven command.

To do so, Maven will download (if it is not already) a clean WildFly distribution to the `target` folder, configure it, start it and deploy the application on it.

This makes very easy and straightforward to manually test the application.

#### Configure a local MySQL
To execute the application you need a MySQL server running in `localhost` and using the default port (3306).

In this server you have to create a database named `dreimt` accessible for the `dreimt` user using the `dreimtpass` password.

This can be configured executing the follow SQL sentences in your MySQL:

```SQL
CREATE DATABASE dreimt;
GRANT ALL ON dreimt.* TO dreimt@localhost IDENTIFIED BY 'dreimtpass';
FLUSH PRIVILEGES;
```

Of course, this configuration can be changed in the POM file.

#### Development database initialization

The `additional-material/db` contains several files related with the DREIMT db initialization. After creating a new database as described in the previous step, you can populate it with the following commands:

```
sudo mysql dreimt < additional-material/db/dreimt-db-initialization.sql
sudo mysql dreimt < additional-material/db/dreimt-schema.sql
zcat additional-material/db/dreimt-data.sql.gz | sudo mysql dreimt
sudo mysql dreimt < additional-material/db/fill_full_drug_signature_interaction_table.sql
```

#### Building the application
The application can be built with the following Maven command:

```
mvn clean install
```

This will build the application launching the tests on a **Wildfly 10.1.0** server.

#### Starting the application
The application can be started with the following Maven command:

```
mvn package wildfly:start wildfly:deploy-only -P wildfly-mysql-run
```

This will start a **WildFly 10.1.0** serving the DREIMT application.

#### Redeploying the application
Once it is running, the application can be re-deployed with the following Maven command:

```
mvn package wildfly:deploy-only -P wildfly-mysql-run
```

#### Stopping the application
The application can be stopped with the following Maven command:

```
mvn wildfly:shutdown
```

#### REST API documentation
The REST API is documented using the [Swagger](https://swagger.io/) framework. It can be browsed using the [Swagger UI](http://petstore.swagger.io/) application to access the following URL:

```
http://localhost:8080/dreimt-backend/rest/api/swagger.json
```
