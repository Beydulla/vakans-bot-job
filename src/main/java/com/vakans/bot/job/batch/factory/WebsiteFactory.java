package com.vakans.bot.job.batch.factory;

import com.vakans.bot.job.batch.data.constants.WebsiteName;
import com.vakans.bot.job.batch.service.website.WebsiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class WebsiteFactory {

    @Autowired
    private List<WebsiteService> websiteServiceList;

    private final Map<WebsiteName, WebsiteService> websiteServiceMap = new HashMap<>();


    @PostConstruct
    public void initActions(){
        for(final WebsiteService service : websiteServiceList){
            websiteServiceMap.put(service.getName(), service);
        }
    }

    public WebsiteService getWebsiteService(final WebsiteName websiteName){
       return websiteServiceMap.get(websiteName);
    }
}
