package com.imthiagu.microservices.currencyconversion.controller;

import com.imthiagu.microservices.currencyconversion.CurrencyExchangeProxy;
import com.imthiagu.microservices.currencyconversion.beans.CurrencyConversion;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;

@RestController
public class CurrencyConversionController {

    private final Logger logger = LoggerFactory.getLogger(CurrencyConversionController.class);
    @Autowired
    private CurrencyExchangeProxy proxy;

    @GetMapping(path = "/currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
    //@Retry(name = "fallbackCalculateCurrencyConversionApi", fallbackMethod = "fallbackCalculateCurrencyConversion")
    @CircuitBreaker(name = "fallbackCalculateCurrencyConversionApi", fallbackMethod = "fallbackCalculateCurrencyConversion")
    public CurrencyConversion calculateCurrencyConversion(@PathVariable String from,
                                                          @PathVariable String to,
                                                          @PathVariable BigDecimal quantity) {
        logger.info("calculateCurrencyConversion");

        HashMap uriVariables = new HashMap();
        uriVariables.put("from",from);
        uriVariables.put("to", to);
        ResponseEntity<CurrencyConversion> responseEntity = new RestTemplate().getForEntity("http://localhost:8000/currency-exchange/{from}/to/{to}" ,CurrencyConversion.class, uriVariables);


        CurrencyConversion currencyConversion = responseEntity.getBody();
        return new CurrencyConversion(currencyConversion.getId(),
                currencyConversion.getFrom(), currencyConversion.getTo(),
                currencyConversion.getConversionMultiple(),
                quantity, quantity.multiply(currencyConversion.getConversionMultiple()), currencyConversion.getEnvironment());
    }

    @GetMapping(path = "/currency-conversion-feign/from/{from}/to/{to}/quantity/{quantity}")
    //@Retry(name ="fallbackCalculateCurrencyConversionFeignApi", fallbackMethod = "fallbackCalculateCurrencyConversionFeign")
    @CircuitBreaker(name ="fallbackCalculateCurrencyConversionFeignApi", fallbackMethod = "fallbackCalculateCurrencyConversionFeign")
    public CurrencyConversion calculateCurrencyConversionFeign(@PathVariable String from,
                                                          @PathVariable String to,
                                                          @PathVariable BigDecimal quantity) {

        logger.info("calculateCurrencyConversionFeign");
        CurrencyConversion currencyConversion = proxy.retrieveExchangeValue(from,to);
        return new CurrencyConversion(currencyConversion.getId(),
                currencyConversion.getFrom(), currencyConversion.getTo(),
                currencyConversion.getConversionMultiple(),
                quantity, quantity.multiply(currencyConversion.getConversionMultiple()), currencyConversion.getEnvironment() + " feign");
    }

    public CurrencyConversion fallbackCalculateCurrencyConversionFeign(String from, String to, BigDecimal quantity,Exception ex) {
        CurrencyConversion currencyConversion = new CurrencyConversion() ;
        currencyConversion.setException("Feign" + ex.getMessage() );
        return currencyConversion;
    }

    public CurrencyConversion fallbackCalculateCurrencyConversion(String from, String to, BigDecimal quantity,Exception ex) {
        CurrencyConversion currencyConversion = new CurrencyConversion() ;
        currencyConversion.setException("No Feign" + ex.getMessage());
        return currencyConversion;
    }


}
