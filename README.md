# Java application + mysql + springboot

## Installation

### Prerequisites

- [Docker](https://www.docker.com/)
- [Docker Compose](https://docs.docker.com/compose/)
- [Java 17](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html)
- [Maven](https://maven.apache.org/download.cgi)

## Launch for development purpose

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

## Launch for production purpose (docker-compose)

Ensure the user, password and database name are the same as the ones in the application.properties file.
You must have `spring.datasource.url = jdbc:mysql://mydatabase:3306/mydatabase` in the application.properties file.

```bash
docker-
```

### Launch the application

```bash
docker run --name myapp --network custom-network -p 8080:8080 myspringbootapp
```


## Database model

Here is the database model for the application. Attributes in bold are **<span style="color:red">primary keys</span>**.

Staff = {
	**<span style="color:red">email</span>**:string,
	passwordHash:string,
	firstName:string,
	lastName:string,
	position:string,
	processesIDs:number[]
}

Patient = {
	**<span style="color:red">email</span>**:string,
	**<span style="color:red">firstName</span>**:string,
	**<span style="color:red">lastName</span>**:string,
	**<span style="color:red">age</span>**:string,
	sex:boolean,
}

Process = {
	**<span style="color:red">id</span>**:number,
	name:string,
	patientEmail:string,
	staffEmails:string[],
	stagesIDs:number[]
}

Stage = {
	**<span style="color:red">id</span>**:number,
	name:string,
	completed:boolean,
	staffEmail:string
}

## Some remarks
- In this model if, for example, a children is sick and the parents too, they can use the same email.
- For the staff we consider the mail as the primary key, which is good because we consider that in a same hospital there can be two people with the same name but not with the same mail.