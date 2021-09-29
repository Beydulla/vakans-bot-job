package com.vakans.bot.job.batch.rowmapper;

import com.vakans.bot.job.batch.data.Filter;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FilterRowMapper implements RowMapper<Filter> {

    @Override
    public Filter mapRow(final ResultSet resultSet, final int i) throws SQLException {
        final Filter filter = new Filter();
        filter.setTags(resultSet.getString("TAGS"));
        filter.setCompany(resultSet.getString("COMPANY"));
        filter.setMaximumSalary(resultSet.getInt("MAXIMUM_SALARY"));
        filter.setMinimumSalary(resultSet.getInt("MINIMUM_SALARY"));
        filter.setTelegramChatId(resultSet.getLong("CHAT_ID"));
        return filter;
    }
}
