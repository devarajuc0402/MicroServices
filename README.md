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

================================================================================================

# Run app in command line:
mvn clean
mvn clean install
mvn spring-boot:start
mvn spring-boot:stop

================================================================================================

# Order to execution the app:
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
