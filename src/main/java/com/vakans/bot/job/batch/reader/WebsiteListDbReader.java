package com.vakans.bot.job.batch.reader;

import com.vakans.bot.job.batch.data.Website;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;


import javax.sql.DataSource;

public class WebsiteListDbReader implements ItemReader<Website> {

    @Autowired
    private DataSource dataSource;

    @Override
    public Website read(){
        return null;
    }
}
