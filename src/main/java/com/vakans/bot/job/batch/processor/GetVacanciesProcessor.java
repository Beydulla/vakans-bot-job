package com.vakans.bot.job.batch.processor;

import com.vakans.bot.job.batch.data.Vacancy;
import com.vakans.bot.job.batch.data.Website;
import com.vakans.bot.job.batch.factory.WebsiteFactory;
import com.vakans.bot.job.batch.service.website.WebsiteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class GetVacanciesProcessor implements ItemProcessor<Website, List<Vacancy>> {

    private final static Logger LOGGER = LoggerFactory.getLogger(GetVacanciesProcessor.class);

    @Autowired
    private WebsiteFactory websiteFactory;

    @Override
    public List<Vacancy> process(final Website website) {
        LOGGER.info("Getting vacancies from {}", website.getName());
        final WebsiteService websiteService = websiteFactory.getWebsiteService(website.getName());
        final List<Vacancy> vacancies = websiteService.getNewVacancies();
        LOGGER.info("{} new vacancies fetched", vacancies.size());
        return vacancies;
    }
}
