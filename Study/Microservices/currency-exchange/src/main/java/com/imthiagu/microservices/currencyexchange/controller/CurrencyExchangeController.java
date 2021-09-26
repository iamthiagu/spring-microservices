package com.imthiagu.microservices.currencyexchange.controller;

import com.imthiagu.microservices.currencyexchange.beans.CurrencyExchange;
import com.imthiagu.microservices.currencyexchange.repository.CurrencyExchangeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CurrencyExchangeController {

    private Logger logger = LoggerFactory.getLogger(CurrencyExchangeController.class);

    @Autowired
    private Environment environment;

    @Autowired
    private CurrencyExchangeRepository repository;

    @GetMapping(path = "/currency-exchange/{from}/to/{to}")
    public CurrencyExchange retrieveExchangeValue(@PathVariable String from, @PathVariable String to) {
        logger.info("Inside currency exchange of retrieveExchangeValue" + from + " "+ to);
        CurrencyExchange currenyExchange = repository.findByFromAndTo(from,to);
        currenyExchange.setEnvironment(environment.getProperty("local.server.port"));
        return currenyExchange;
    }
}
