# MicroServices:
I. Microservices with Java 21 and Spring Boot 4 
	a. Modules: 
		1. help-service
    	2. kafka-service
    	3. restclient-service
    	4. config-server
    	5. eureka-client-api-gateway
    	7. eureka-server-service-discovery
	b. Branch: master

================================================================================================

# Technologies:
Java 21
Spring boot 4
Microservices
Oracle DB 19c
Kafka
Rest API
Restclient
Docker

================================================================================================

# Run app in command line:
mvn clean
mvn clean install
mvn spring-boot:start
mvn spring-boot:stop

mvn test

================================================================================================

# Order to execution the app:
a. Start Kafka in command:
	..\bin\windows\kafka-server-start.bat ..\config\server.properties
b. Start redis on docker:
	Start: docker start redis
	Verify: docker exec -it redis redis-cli ping - PONG
	Running status: docker ps
c. Start oauth2-keycloak on docker:

-Modules:
a. config-server
b. eureka-server-service-discovery
c. eureka-client-api-gateway
d. help-service
e. restclient-service
f. kafka-service

================================================================================================

# Module Health or Info check urls: (Local environment)
a. config-server:
	http://localhost:8888/actuator/health
	http://localhost:8888/actuator/info
b. eureka-server-service-discovery:
	http://localhost:8761/actuator/health
	http://localhost:8761/actuator/info
c. eureka-client-api-gateway
	http://localhost:8080/actuator/health
	http://localhost:8080/actuator/info
	http://localhost:8080/actuator/gateway/routes
d. help-service: 
	http://localhost:8081/actuator/health
	http://localhost:8081/actuator/info
e. restclient-service:
	http://localhost:8082/actuator/health
	http://localhost:8082/actuator/info
f. kafka-service: 
	http://localhost:8083/actuator/health
	http://localhost:8083/actuator/info

================================================================================================

# Gateway Endpoint urls: Manual Discovery config (Local environment)
a. help-service: http:localhost:8080/api/help/**
b. restclient-service: http:localhost:8080/api/restclient/**
c. kafka-service: http:localhost:8080/api/kafka/**

-Manual config:
spring.cloud.gateway.server.webflux.routes[0].id=help-service
spring.cloud.gateway.server.webflux.routes[0].uri=lb://help-service
spring.cloud.gateway.server.webflux.routes[0].predicates[0]=Path=/api/help/**

================================================================================================

# Gateway Endpoint urls: Auto Discovery config (Local environment)
a. help-service: http://localhost:8080/help-service/api/help/**
b. restclient-service: http://localhost:8080/restclient-service/api/restclient/**
c. kafka-service: http://localhost:8080/kafka-service/api/kafka/**

-Auto config:
spring.cloud.gateway.server.webflux.discovery.locator.enabled=true
spring.cloud.gateway.server.webflux.discovery.locator.lower-case-service-id=true

================================================================================================

# Eureka Server url: (Local environment)
http://localhost:8761/

================================================================================================

# Circuit Breaker:
-Check in API gateway logs: 
CircuitBreaker 'helpCircuitBreaker' changed state from CLOSED to OPEN

URL: http://localhost:8080/actuator/metrics/resilience4j.circuitbreaker.state?tag=name:
		CIRCUIT_BREAKER_NAME&tag=state:closed
		CIRCUIT_BREAKER_NAME&tag=state:open
		CIRCUIT_BREAKER_NAME&tag=state:half_open

a. help-service:
	-Its downstream module there is no external api
a. restclient-service: 
	http://localhost:8080/api/restclient/message
b. kafka-service: 
	http://localhost:8080/api/kafka/message

1-Active
0-Inactive

message: Help Service is currently unavailable. Please try again later.

================================================================================================

# Redis - Rate Limiter:
URL: http://localhost:6379/

-Run on docker: docker run -d --name redis -p 6379:6379 redis:latest
-Test redis: docker exec -it redis redis-cli
-127.0.0.1:6379>: ping --> PONG
-exit

================================================================================================

# Docker:
a. redis:
	-redis image Running on docker: http://localhost:6379/
a. oauth2-keycloak:
	-keycloak image Running on docker: http://localhost:8180/	

================================================================================================

# OAuth2 - Keycloak security domain:
URL: http://localhost:8180/
document path: ..\microservices-parent\docs\oauth2_keycloak.txt

realm url: http://localhost:8180/realms/microservices
realm name: microservices
client name: microservices-client
User name: testuser
Roles: [USER, ADMIN, HELP, KAFKA, REST]

================================================================================================

# Junit5:
document path: ..\microservices-parent\docs\junit5.txt

a. Testing : 
	-Open particular module folder
	-Run: mvn test

b. Default: It will fetch property file
	-src/main/resources
	-Then trigger from config-server
	
b. Flow: 
	i. config-server 
	ii. eureka-server-service-discovery 
		-Start first config-server 
	iii. eureka-client-api-gateway 
		-Start first config-server, eureka-server-service-discovery
	iv. help-service 
		-Start first config-server, eureka-server-service-discovery, eureka-client-api-gateway
	v. restclient-service 
		-Start first config-server, eureka-server-service-discovery, eureka-client-api-gateway
	vi. kafka-service  
		-Start first config-server, eureka-server-service-discovery, eureka-client-api-gateway
	

================================================================================================

# Postman:
a. eureka-client-api-gateway
	i. oauth2-keycloak authentication
		-To generate token
		-document path: ..\microservices-parent\docs\oauth2_keycloak.txt
		-used to store body & header through request and collection

================================================================================================

# Swagger urls: (Local environment)
a. help-service: http://localhost:8081/swagger-ui/index.html#
a. restclient-servic: http://localhost:8082/swagger-ui/index.html#
a. kafka-service: http://localhost:8083/swagger-ui/index.html#

================================================================================================

II. config-repository: https://github.com/devarajuc0402/config-repo.git

================================================================================================

III. Spring-Boot repository: https://github.com/devarajuc0402/SpringBootTest

================================================================================================

IV. Hacker Rank: https://www.hackerrank.com/profile/devarajuc0402

================================================================================================

V. LinkedIn: www.linkedin.com/in/devarajuc

================================================================================================
