package com.vakans.bot.job.batch.dao;

import com.vakans.bot.job.batch.data.Filter;
import com.vakans.bot.job.batch.rowmapper.FilterRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FilterDaoImpl implements FilterDao{

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Value("${query.get.filter.list}")
    private String queryGetFilterList;

    @Override
    public List<Filter> getFilterList() {
        return jdbcTemplate.query(queryGetFilterList, new FilterRowMapper());

    }
}
