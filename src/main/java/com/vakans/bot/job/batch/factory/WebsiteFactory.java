package com.vakans.bot.job.batch.factory;

import com.vakans.bot.job.batch.dao.LastVacancyDao;
import com.vakans.bot.job.batch.service.website.BossAzService;
import com.vakans.bot.job.batch.service.website.JobSearchService;
import com.vakans.bot.job.batch.service.website.WebsiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WebsiteFactory {

    @Autowired
    private LastVacancyDao lastVacancyDao;

    public WebsiteService getWebsiteService(final String websiteName){
        if("boss.az".equals(websiteName.trim())){
            return new BossAzService(lastVacancyDao);
        }else if("jobsearch.az".equals(websiteName)){
            return new JobSearchService(lastVacancyDao);
        }
        return new BossAzService(lastVacancyDao);
    }
}
