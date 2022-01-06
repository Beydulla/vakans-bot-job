package com.vakans.bot.job.batch.service.website;

import com.vakans.bot.job.batch.data.Vacancy;
import com.vakans.bot.job.batch.data.constants.WebsiteName;

import java.util.List;

public interface WebsiteService {

    List<Vacancy> getNewVacancies();

    WebsiteName getName();
}
