package com.vakans.bot.job.batch.dao;

import com.vakans.bot.job.batch.data.LastVacancy;

public interface LastVacancyDao {

    LastVacancy getLastVacancyByWebsite(final String website);

    int updateLastVacancyByWebsite(final String website, final String link);

}
