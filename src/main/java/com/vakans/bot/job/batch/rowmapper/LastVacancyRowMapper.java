package com.vakans.bot.job.batch.rowmapper;

import com.vakans.bot.job.batch.data.LastVacancy;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LastVacancyRowMapper implements RowMapper<LastVacancy> {

    @Override
    public LastVacancy mapRow(final ResultSet resultSet, final int i) throws SQLException {
        final LastVacancy lastVacancy = new LastVacancy();
        lastVacancy.setId(resultSet.getLong("ID"));
        lastVacancy.setLink(resultSet.getString("LINK"));
        lastVacancy.setWebsite(resultSet.getString("WEBSITE"));
        return lastVacancy;
    }
}
