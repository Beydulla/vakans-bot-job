package com.vakans.bot.job.batch.dao;

import com.vakans.bot.job.batch.data.constants.WebsiteName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class GeneralDaoImpl implements GeneralDao{

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Value("${query.get.latest.vacancies.by.website}")
    private String queryGetLatestVacanciesByWebsite;
    @Value("${query.insert.new.vacancy.link}")
    private String queryInsetNewVacancyLink;

    @Override
    public List<String> getLatestVacanciesByWebsite(final WebsiteName websiteName) {
        final Map<String, Object> params = new ConcurrentHashMap<>();
        params.put("WEBSITE", websiteName.toString());
        return jdbcTemplate.queryForList(queryGetLatestVacanciesByWebsite, params, String.class);
    }

    @Override
    public void insertNewVacancyLink(final String link, final WebsiteName websiteName) {
        final Map<String, Object> params = new ConcurrentHashMap<>();
        params.put("LINK", link);
        params.put("WEBSITE", websiteName.toString());
        jdbcTemplate.update(queryInsetNewVacancyLink, params);
    }
}
