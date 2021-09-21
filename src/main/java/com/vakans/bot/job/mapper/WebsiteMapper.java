package com.vakans.bot.job.mapper;

import com.vakans.bot.job.data.Website;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class WebsiteMapper implements RowMapper<Website> {

    @Override
    public Website mapRow(final ResultSet resultSet, final int i) throws SQLException {
        final Website website = new Website();
        website.setId(resultSet.getLong("ID"));
        website.setName(resultSet.getString("NAME"));
        return website;
    }
}
