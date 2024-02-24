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
## API documentation
Api documentation is available online [here](https://csc7437-msc-csn-m2-globallabs-2023-2024.github.io/apidocs/)

## Database model

Here is the database model for the application. Attributes in bold are **<span style="color:red">primary keys</span>**.

Staff = {
	**<span style="color:red">email</span>**:string,
	passwordHash:string,
	firstName:string,
	lastName:string,
	position:string,
	processesIds:number[]
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
	patientId:string,
	staffEmails:string[],
	stagesIds:number[]
}

Stage = {
	**<span style="color:red">id</span>**:number,
	name:string,
	completed:boolean,
	staffEmail:string,
	processId:number
}

## Some remarks
### Remarks about patient
- As the patient primary key is (email, firstName, lastName, age), if for example a children is sick and the parents too, they can use the same email.
- When delete a patient, all processes associated with the patient are deleted
### Remarks about staff
- For the staff we consider the mail as the primary key because we consider that in a same hospital there can be two people with the same name but not with the same mail.
- Only a staff admin can create a staff
- Only an admin or a staff himself can modify the staff profile
- Only an admin can delete a staff
- All staff can get staff profile by email or all staff profiles
- All staff can CRUD (Create/Read/Update/Delete) all processes
- All staff can CRUD all stages
- All staff can CRUD all patient
### Remarks about process
- When create a process, the patient must exist (You cannot create a process if the patient does not exist)
- When a process is deleted, all stages are deleted
### Remarks about stage
- When create a stage, the process must exist
- When delete a stage, the stage id is removed from the process stagesIDs

## Remains to do for the project
- Improve the security of the application:
  - Use JWT for authentication instead of basic authentication with username and password each time calling an endpoint
  - Use HTTPS instead of HTTP
- Add more tests
  - Each have been tested at least once but not all the cases have been tested
- Add logs with libraries like log4j
