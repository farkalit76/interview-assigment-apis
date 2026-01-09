

# Technical Requirement

1. Java : 17
2. Spring Boot : 3.3.4
3. lombok for Slf4J/Log4j is for logging.
4. Swagger API, with proper api comments.
5. SonarQube and SonarList implementation for Java coding standard.
6. Junit test cases with Mockito to test the API.
7. Spring boot 3.3.4 with Hibernate and JpaRepository, security (with header value) implementation.
8. Java Database (Postgres-17) Connection with Hikari connection pool size and its idle size.
9. Caching: Redis 5.1 windows
10. Build with: Maven pom.xml
11. Exception Handling with RestControllerAdvice



### Build Application
mvn clean install


### Run Junit Test
mvn clean verify


### Run the application
mvn spring-boot:run

### Start Redis Server
Before starting application make sure Redis server is up with default hostname and port number

### Start Postgres DB Server
Before starting application make sure Database is up


### Test Application
Got to Browser and open Swagger to execute it
URL: http://localhost:8080/swagger-ui/index.html
Use secret key for testing= my-secret-key


### Actuator and Document URL
Actuator health: http://localhost:8080/actuator/health
Actuator URL: http://localhost:8080/actuator/mappings
Api-docs URL: http://localhost:8080/v3/api-docs


### Run on Docker

Before building image just change these two lines in the application properties file
->spring.datasource.company.url=${datasource_url:jdbc:postgresql://host.docker.internal:5432/companydb?reWriteBatchedInserts=true}
->spring.data.redis.host=host.docker.internal
It is required to call these server from my localhost & port.


#### Build Docker image
docker build -t interview-assignment .

#### Run Container
docker run -p 9090:8080 interview-assignment

#Test the application using URL -> http://localhost:9090/


### To kill the running port
Find->netstat -ano | findstr :8080
Kill->taskkill /PID 12345 /F





