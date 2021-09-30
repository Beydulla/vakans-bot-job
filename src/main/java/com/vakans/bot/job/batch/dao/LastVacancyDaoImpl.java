package com.vakans.bot.job.batch.dao;

import com.vakans.bot.job.batch.data.LastVacancy;
import com.vakans.bot.job.batch.rowmapper.LastVacancyRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class LastVacancyDaoImpl implements LastVacancyDao {

    @Value("${query.get.last.vacancy.by.website}")
    private String queryGetLastVacancyByWebsite;

    @Value("${query.update.last.vacancy.by.website}")
    private String queryUpdateLastVacancyByWebsite;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public LastVacancy getLastVacancyByWebsite(final String website) {
        final Map<String,Object> paramMap = new ConcurrentHashMap<>();
        paramMap.put("WEBSITE", website);
        return jdbcTemplate.queryForObject(queryGetLastVacancyByWebsite, paramMap, new LastVacancyRowMapper());
    }

    @Override
    public int updateLastVacancyByWebsite(final String website, final String link) {
        final Map<String,Object> paramMap = new ConcurrentHashMap<>();
        paramMap.put("LINK", link);
        paramMap.put("WEBSITE", website);
        return jdbcTemplate.update(queryUpdateLastVacancyByWebsite, paramMap);
    }
}
