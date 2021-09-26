package com.imthiagu.microservices.limitsservice.controller;

import com.imthiagu.microservices.limitsservice.bean.Limits;
import com.imthiagu.microservices.limitsservice.configuration.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LimitsController {

    @Autowired
    private Configuration configuration;

    @GetMapping(path = "/limits")
    public Limits retrievelimits() {
     return  new Limits(configuration.getMinimum(), configuration.getMaximum());
    }
}
