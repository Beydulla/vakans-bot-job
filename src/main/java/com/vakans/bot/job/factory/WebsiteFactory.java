package com.vakans.bot.job.factory;

import com.vakans.bot.job.service.website.BossAzService;
import com.vakans.bot.job.service.website.WebsiteService;
import org.springframework.stereotype.Component;

@Component
public class WebsiteFactory {

    public WebsiteService getWebsiteService(final String websiteName){
        if("boss.az".equals(websiteName.trim())){
            return new BossAzService();
        }
        return new BossAzService();
    }
}
