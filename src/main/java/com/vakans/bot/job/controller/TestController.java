package com.vakans.bot.job.controller;

import com.vakans.bot.job.factory.WebsiteFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/test")
@RestController
public class TestController {

    private WebsiteFactory websiteFactory;

    @GetMapping
    public String get(){
        return websiteFactory.getWebsiteService("dfdsf").toString();
    }
}
