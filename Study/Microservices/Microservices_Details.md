**Port:**

Currency-Exchange   : 8000,8101
Currency-Conversion : 8100
Limits-service      : 9091
Spring-cloud-config-server : 8888
Naming-server       : 8761
Api-gateway         : 8765
Zipkin(Docker)      :9411


URL:

Currency-Conversion:
http://localhost:8100/currency-conversion-feign/from/USD/to/INR/quantity/10
http://localhost:8100/currency-conversion/from/USD/to/INR/quantity/10

Currency-Exchange:
http://localhost:8000/currency-exchange/USD/to/INR
http://localhost:8101/currency-exchange/USD/to/INR

Api-Gateway:
** All th api will have some common features such as security , monitoring etc and that can be define in api-gateway **
http://localhost:8765/currency-exchange/currency-exchange/USD/to/INR

http://localhost:8765/currency-conversion/currency-conversion-feign/from/USD/to/INR/quantity/10
http://localhost:8765/currency-conversion/currency-conversion/from/USD/to/INR/quantity/10

#Lowercase

Add below property in app.properties
spring.cloud.gateway.discovery.locator.lowerCaseServiceId=true

http://localhost:8765/currency-exchange/currency-exchange/USD/to/INR

http://localhost:8765/currency-conversion/currency-conversion-feign/from/USD/to/INR/quantity/10
http://localhost:8765/currency-conversion/currency-conversion/from/USD/to/INR/quantity/10


#Currency exchange:

#Currency conversion - 
Has @EnableFeignClients in spring boot App file and Interface Proxy with Feignclient= "microservice app name" 
and signature with mappings.

#Naming server - 
also known as service registry. @EnableEurekaServer in spring boot App file to register microservices with 
eureka server add eureka client dependency on other microservices. To not register itself add
eureka.client.register-with-eureka=false
eureka.client.fetch-registry=false

#Api gateway - 
Add spring-cloud-starter-gateway dependency to the api gateway. Add LogFilter by implementing Global filter
and implementing its methods.
It is mainly used to do security, monitoring/metrics, logging etc for all the apis. This needs to be enabled by adding 
below spring.cloud.gateway.discovery.locator.enabled=true.

#Spring cloud config server
Add spring-cloud-config-server dependency to Spring cloud config server. Add @EnableConfigServer adn add the git hib url to
fetch the properties dynamically at the runtime.
spring.cloud.config.server.git.uri=file:///Users/thiasubr/Study/Microservices/git-localconfig-repo

In other microservices where the config is used need to have add Configuration class and 
add @ConfigurationProperties("limits-service").

In app.properties add 
spring.cloud.config.profile=qa
spring.config.import = optional:configserver:http://localhost:8888

Based on the profile respective property file will be picked up.

#Zipkin  Distributed tracing
localhost:9411/zipkin

#Circuit-breaker -

Add resilence4j dependency to the app.
**Do @Retry(name = "default or it can any specific api name",
fallbackmethod = "anymethod but should be present in same controller")
fallback method shou;d have same parameters as in api with extra exception object.

With fallback method can send the response back to client after the certain retry.Response should be in same format as api.

resilience4j.retry.instances.fallbackCalculateCurrencyConversionApi.maxRetryAttempts=10
resilience4j.retry.instances.fallbackCalculateCurrencyConversionApi.waitDuration=1s
resilience4j.retry.instances.fallbackCalculateCurrencyConversionApi.enableExponentialBackoff=true


@CircuitBreaker(name = "default or it can any specific api name",
fallbackmethod = "anymethod but should be present in same controller")
CircuitBreaker three states (Open,closed,halfopen)
By default it will be in closed state.
If requests to an specific api is failing continously it will go into open state.
After some time open half and send some requests to api if reponse is sent back, it will go into closed state again.
**If reuquests are failing continously it will just return default response won't hit call the api


