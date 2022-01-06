package com.vakans.bot.job.batch.rowmapper;

import com.vakans.bot.job.batch.data.Website;
import com.vakans.bot.job.batch.data.constants.WebsiteName;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class WebsiteMapper implements RowMapper<Website> {

    @Override
    public Website mapRow(final ResultSet resultSet, final int i) throws SQLException {
        final Website website = new Website();
        website.setId(resultSet.getLong("ID"));
        website.setName(WebsiteName.valueOf(resultSet.getString("NAME")));
        return website;
    }
}
