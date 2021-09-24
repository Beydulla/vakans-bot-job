package com.vakans.bot.job.processor;

import com.vakans.bot.job.data.Vacancy;
import com.vakans.bot.job.data.Website;
import com.vakans.bot.job.factory.WebsiteFactory;
import com.vakans.bot.job.service.website.WebsiteService;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class GetVacanciesProcessor implements ItemProcessor<Website, List<Vacancy>> {

    @Autowired
    private WebsiteFactory websiteFactory;

    @Override
    public List<Vacancy> process(final Website website) {
        final WebsiteService websiteService = websiteFactory.getWebsiteService(website.getName());
        return websiteService.getNewVacancies();
    }
}
