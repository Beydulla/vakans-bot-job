package com.vakans.bot.job.writer;

import com.vakans.bot.job.data.Vacancy;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

public class VacancyWriter implements ItemWriter<List<Vacancy>> {


    @Override
    public void write(List<? extends List<Vacancy>> list) throws Exception {

    }
}
