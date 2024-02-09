# Java application + mysql + springboot

## Installation

### Prerequisites

- [Docker](https://www.docker.com/)
- [Docker Compose](https://docs.docker.com/compose/)
- [Java 17](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html)
- [Maven](https://maven.apache.org/download.cgi)

## For development purposes

### Launch the database

```bash
docker run --name mydatabase -e MYSQL_ROOT_PASSWORD=secret -e MYSQL_USER=myuser -e MYSQL_PASSWORD=secret -e MYSQL_DATABASE=mydatabase -e MYSQL_ALLOW_EMPTY_PASSWORD=yes -d -p 3306:3306 mysql:latest
```
Ensure the user, password and database name are the same as the ones in the application.properties file.
You must have `spring.datasource.url = jdbc:mysql://localhost:3306/mydatabase` in the application.properties file.
### Launch the application with maven

```bash
mvn clean install
mvn spring-boot:run
```

## Full containerized environment

### Create a custom network

```bash
docker network create custom-network
```

### Launch the database

```bash
docker run --name mydatabase --network custom-network -e MYSQL_ROOT_PASSWORD=secret -e MYSQL_USER=myuser -e MYSQL_PASSWORD=secret
-e MYSQL_DATABASE=mydatabase -e MYSQL_ALLOW_EMPTY_PASSWORD=yes -d -p 3306:3306 mysql:latest
```

### Build the application
Ensure the user, password and database name are the same as the ones in the application.properties file.
You must have `spring.datasource.url = jdbc:mysql://mydatabase:3306/mydatabase` in the application.properties file.

```bash
docker build -t myspringbootapp .
```

### Launch the application

```bash
docker run --name myapp --network custom-network -p 8080:8080 myspringbootapp
```


## database model
Staff = {
	email:string,
	passwordHash:string,
	firstName:string,
	lastName:string,
	position:string,
	processesIDs:number[]
}

Patient = {
	email:string
	firstName:string,
	lastName:string,
	sex:boolean,
    age:number,
}

Process = {
	id:number,
	name:string,
	patientEmail:string,
	staffEmails:string[],
	stagesIDs:number[]
}

Stage = {
	id:number,
	name:string,
	completed:boolean,
	staffEmail:string
}

