# Java application + mysql + springboot

## Installation

### Prerequisites

- [Docker](https://www.docker.com/)
- [Docker Compose](https://docs.docker.com/compose/)
- [Java 17](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html)
- [Maven](https://maven.apache.org/download.cgi)

### Launch the database

```bash
docker run --name mydatabase -e MYSQL_ROOT_PASSWORD=secret -e MYSQL_USER=myuser -e MYSQL_PASSWORD=secret -e MYSQL_DATABASE=mydatabase -e MYSQL_ALLOW_EMPTY_PASSWORD=yes -d -p 3306:3306 mysql:latest
```

### Launch the application

```bash
mvn clean install
mvn spring-boot:run
```

### database model
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

