<h1 align="center">
  <img src="https://github.com/puffproject/docs/blob/master/logo.png" height="200"/><br>
  Puff
</h1>

<h4 align="center">Open source smoke testing platform</h4>

<!-- TODO Add link to platform >
<!-- <h4 align="center">Open source smoke testing platform | <a href="LINK" target="_blank">LINK</a></h4> -->

<p align="center">
  <a href="https://www.oracle.com/ca-en/java/technologies/javase/javase-jdk8-downloads.html" rel="nofollow"><img src="https://img.shields.io/badge/java-1.8-009ACD?style=flat-square&logo=Java" alt="java version" data-canonical-src="https://img.shields.io/badge/java-1.8-f39f37?style=flat-square&logo=Java" style="max-width:100%;"></a>
  <a href="https://spring.io/projects/spring-boot" rel="nofollow"><img src="https://img.shields.io/badge/spring--boot-3.2.0-6db33f?style=flat-square&logo=Spring" alt="spring boot version" data-canonical-src="https://img.shields.io/badge/spring--boot-3.2.0-6db33f?style=flat-square&logo=Spring" style="max-width:100%;"></a>
  <a href="https://swagger.io" rel="nofollow"><img src="https://img.shields.io/badge/swagger-2.0-6c9a00?style=flat-square&logo=Swagger" alt="swagger version" data-canonical-src="https://img.shields.io/badge/swagger-2.0-6c9a00?style=flat-square&logo=Swagger" style="max-width:100%;"></a>
</p>

<blockquote align="center">
  <em>Puff</em> is an open source smoke testing platform for students to collaboratively write and run tests on their assignment or project code for quick and easy sanity testing.
</blockquote>

# Course-management

[![Build & Tests](https://img.shields.io/github/workflow/status/puffproject/course-management/Build%20&%20Test?label=build%20%26%20tests)](https://github.com/puffproject/course-management/actions/workflows/build.yml)
[![Code coverage](https://codecov.io/gh/puffproject/course-management/branch/master/graph/badge.svg?token=YRCB9LTSNC)](https://codecov.io/gh/puffproject/course-management)

Spring-boot microservice managing courses, assignments and user actions for the puff platform. For the full overview of the puff project see the [docs repository](https://github.com/puffproject/docs).

## Getting started
Clone the project with `https://github.com/puffproject/course-management.git`

### Install Java
You'll need Java to run _Puff's_ microservices developed with [Spring Boot](https://spring.io/projects/spring-boot).

* Download and install the [Java JDK 8](https://www.oracle.com/ca-en/java/technologies/javase/javase-jdk8-downloads.html)
* Set the `JAVA_HOME` environment variable
* Verify your installation by running `java -version`

### Install Maven
_Puff_ uses [Maven](https://maven.apache.org/) as its build tool for its backend.
* [Download](https://maven.apache.org/download.cgi) and [install](https://maven.apache.org/install.html) Maven
* Verify your installation with `mvn -v`

### Setup Keycloak (If you haven't already)
> Keycloak is an open source Identity and Access Management solution aimed at modern applications and services. It makes it easy to secure applications and services with little to no code.

_Puff_ uses keycloak as a user management and authentication solution. More information about Keycloak can be found on their [offical docs page](https://www.keycloak.org/docs/latest/index.html).

Follow the instructions found at https://github.com/puffproject/docs#setup-keycloak to setup a local keycloak server. This only needs to be configured once.

Once configured, generate an authentication token by making the following curl call **replacing TEST_USER_USERNAME**, **TEST_USER_PASSWORD** and **USER_AUTH_CLIENT_SECRET** with the credentials for the test accounts you created and the client-secret for user-auth.

```shell
curl -X POST 'http://localhost:8180/auth/realms/puff/protocol/openid-connect/token' \
 --header 'Content-Type: application/x-www-form-urlencoded' \
 --data-urlencode 'grant_type=password' \
 --data-urlencode 'client_id=user-auth' \
 --data-urlencode 'client_secret=USER_AUTH_CLIENT_SECRET' \
 --data-urlencode 'username=TEST_USER_USERNAME' \
 --data-urlencode 'password=TEST_USER_PASSWORD'
```
### Run the backend
In order to run a microservice locally run
```
mvn spring-boot:run -Dspring-boot.run.profiles=local
```
* If you need to build the `.jar` of the application run `mvn package`.
* In order to run tests run `mvn test`.

## Development

### Swagger
_Puff_'s Spring-Boot backend exposes a REST API. The project utilizes [Swagger](https://swagger.io/) to document and keep a consistent REST interface.

Once you have a microservice running (See [run the backend](#run-the-backend)) visit http://localhost:8080/swagger-ui.html. A `json` api version to be consumed and used to generate client libraries can be accessed at http://localhost:8080/v2/api-docs. Select `Authorize` and login with a test user account to try out any of the endpoints.

### H2 Database
_Puff_'s Spring-boot backend uses a H2 runtime database to simulate a database connection for local development. Once the project is running it can be accessed at http://localhost:8080/h2.

The credentials for the database are as follows:
```
Driver Class: org.h2.Driver
JDBC URL: jdbc:h2:mem:testdb
User Name: admin
Password:
```

For more information about H2 databases see the [H2 Database Engine](https://www.h2database.com/html/main.html).

### Formatting

The codebase is auto-formatted with the [formatter-maven-plugin](https://code.revelc.net/formatter-maven-plugin/) that will format all source code files in the `src/` and `test/` directories according to the settings in the `style.xml` file, which are based on eclipse profile settings.

Run the `formatter:format` command to run the formatter. It is also bound to the `format` goal that will run as part of the `compile` phase. 

You can also add the git hook in `.hooks` to your local `.git/hooks` folder to run the formatter on pre-commit.

## Contributors
The _Puff_ project is looking for contributors to join the initiative!
For information about progress, features under construction and opportunities to contribute see [our project board](https://github.com/orgs/puffproject/projects/1).


If you're interested in helping please read our [CONTRIBUTING.md](./CONTRIBUTING.md) for details like our Code of Conduct and contact [Benjamin Kostiuk](mailto:benkostiuk1@gmail.com) for more information.