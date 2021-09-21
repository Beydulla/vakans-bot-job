package com.vakans.bot.job.reader;

import com.vakans.bot.job.data.Website;
import com.vakans.bot.job.mapper.WebsiteMapper;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;


import javax.sql.DataSource;
import java.util.List;

public class WebsiteListDbReader implements ItemReader<Website> {

    @Autowired
    private DataSource dataSource;

    @Override
    public Website read(){
        return null;
    }
}
