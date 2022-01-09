package com.vakans.bot.job.batch.config;

import com.vakans.bot.job.batch.data.Message;
import com.vakans.bot.job.batch.data.Website;
import com.vakans.bot.job.batch.listener.SpringBatchJobCompletionListener;
import com.vakans.bot.job.batch.processor.FilterVacanciesProcessor;
import com.vakans.bot.job.batch.processor.GetVacanciesProcessor;
import com.vakans.bot.job.batch.rowmapper.WebsiteMapper;
import com.vakans.bot.job.batch.writer.VacancyWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableBatchProcessing
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
        return stepBuilderFactory.get("vacanciesStep").< Website, List<Message>>chunk(1)
                .reader(websiteListDbReader())
                .processor(compositeItemProcessor())
                .writer(vacancyWriter()).build();
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
                .sql("SELECT * FROM WEBSITE WHERE ACTIVE = 1")
                .rowMapper(new WebsiteMapper())
                .build();
    }

    @Bean
    public GetVacanciesProcessor getVacanciesProcessor(){
        return new GetVacanciesProcessor();
    }

    @Bean
    public FilterVacanciesProcessor filterVacanciesProcessor(){
        return new FilterVacanciesProcessor();
    }

    @Bean
    public ItemProcessor<Website, List<Message>> compositeItemProcessor(){
        final CompositeItemProcessor<Website, List<Message>> itemProcessor = new CompositeItemProcessor<>();
        itemProcessor.setDelegates(Arrays.asList(getVacanciesProcessor(), filterVacanciesProcessor()));
        return itemProcessor;
    }

    @Bean
    public ItemWriter<List<Message>> vacancyWriter(){
        return new VacancyWriter();
    }

    @Bean
    public RestTemplate restTemplate() {
        final RestTemplate restTemplate =  new RestTemplate();
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON, MediaType.TEXT_HTML));
        messageConverters.add(converter);
        restTemplate.setMessageConverters(messageConverters);
        return restTemplate;
    }
}