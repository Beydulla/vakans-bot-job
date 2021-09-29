package com.vakans.bot.job.batch.factory;

import com.vakans.bot.job.batch.service.website.BossAzService;
import com.vakans.bot.job.batch.service.website.WebsiteService;
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
