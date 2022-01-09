package com.vakans.bot.job.batch.dao;

import com.vakans.bot.job.batch.data.constants.WebsiteName;

import java.util.List;

public interface GeneralDao {

    List<String> getLatestVacanciesByWebsite(final WebsiteName websiteName);

    void insertNewVacancyLink(final String link, final WebsiteName websiteName);
}
