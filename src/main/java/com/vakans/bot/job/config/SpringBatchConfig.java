package com.vakans.bot.job.config;

import com.vakans.bot.job.data.Vacancy;
import com.vakans.bot.job.data.Website;
import com.vakans.bot.job.listener.SpringBatchJobCompletionListener;
import com.vakans.bot.job.processor.GetVacanciesProcessor;
import com.vakans.bot.job.writer.VacancyWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;
import java.util.List;

@Configuration
public class SpringBatchConfig {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    private DataSource dataSource;

    @Bean
    public Job vacancyJob() {
        return jobBuilderFactory.get("vacancyJob")
                        .incrementer(new RunIdIncrementer())
                        .listener(listener())
                        .flow(vacanciesStep()).end().build();
    }

    @Bean
    public Step vacanciesStep() {
        return stepBuilderFactory.get("vacanciesStep"). < Website, List<Vacancy>> chunk(1)
                .reader(websiteListDbReader())
                .processor(getVacanciesProcessor())
                .writer(new VacancyWriter()).build();
    }

    @Bean
    public JobExecutionListener listener() {
        return new SpringBatchJobCompletionListener();
    }

    @Bean
    public ItemReader<Website> websiteListDbReader() {
        return new JdbcCursorItemReaderBuilder<Website>()
                .name("websiteListDbReader")
                .dataSource(dataSource)
                .sql("SELECT * FROM WEBSITE")
                .rowMapper(new BeanPropertyRowMapper<>(Website.class))
                .build();
    }

    @Bean
    public GetVacanciesProcessor getVacanciesProcessor(){
        return new GetVacanciesProcessor();
    }

}