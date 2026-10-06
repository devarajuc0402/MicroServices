package com.org.help.restclient.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.org.help.restclient.client.HelpServiceClient;

@RestController
@RequestMapping("/api/restclient")
public class HelpRestClientController {

    private final HelpServiceClient helpServiceClient;

    public HelpRestClientController(HelpServiceClient helpServiceClient) {
        this.helpServiceClient = helpServiceClient;
    }

    @RequestMapping(value = "/message", method = RequestMethod.GET)
    public String getMessage() {
        return helpServiceClient.getMessage();
    }
}