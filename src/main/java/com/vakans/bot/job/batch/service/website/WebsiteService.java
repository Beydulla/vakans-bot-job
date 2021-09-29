package com.vakans.bot.job.batch.service.website;

import com.vakans.bot.job.batch.data.Vacancy;

import java.util.List;

public interface WebsiteService {

    List<Vacancy> getNewVacancies();
}
