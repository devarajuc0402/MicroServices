package com.org.help.kafka.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.org.help.kafka.client.HelpServiceClient;

@RestController
@RequestMapping("/api/kafka")
public class HelpKafkaController {

    private final HelpServiceClient helpServiceClient;

    public HelpKafkaController(HelpServiceClient helpServiceClient) {
        this.helpServiceClient = helpServiceClient;
    }

    @RequestMapping(value = "/message", method = RequestMethod.GET)
    public String getMessage() {
        return helpServiceClient.getMessage();
    }
}