package com.vakans.bot.job.service.website;

import com.vakans.bot.job.data.Vacancy;

import java.util.List;

public interface WebsiteService {

    List<Vacancy> getNewVacancies();
}
