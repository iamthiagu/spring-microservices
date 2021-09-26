package com.imthiagu.microservices.currencyexchange.controller;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class CircuitBreakerController {

    private Logger logger  = LoggerFactory.getLogger(CircuitBreakerController.class);
    @GetMapping(path = "/sample-api")
    //@Retry(name = "sample-api" , fallbackMethod = "hardCodedFallbackResponse")
    @CircuitBreaker(name = "sample-api" , fallbackMethod = "hardCodedFallbackResponse")
    public String sampleApi() {
        logger.info("Sample api call received");
        ResponseEntity<String> responseEntity = new RestTemplate().getForEntity("http://localhost:8100/dummy-uri",String.class);
        return responseEntity.getBody();
        }

    public String hardCodedFallbackResponse(Exception ex) {
        return "Fallback Response";
    }
}
