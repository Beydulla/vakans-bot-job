package com.vakans.bot.job.batch.config;

import com.vakans.bot.job.batch.data.Message;
import com.vakans.bot.job.batch.data.Website;
import com.vakans.bot.job.batch.listener.SpringBatchJobCompletionListener;
import com.vakans.bot.job.batch.processor.FilterVacanciesProcessor;
import com.vakans.bot.job.batch.processor.GetVacanciesProcessor;
import com.vakans.bot.job.batch.writer.VacancyWriter;
import com.vakans.bot.job.polling.TelegramPollingBot;
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
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.sql.DataSource;
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

    @Autowired
    private TelegramPollingBot telegramPollingBot;

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
                .sql("SELECT * FROM WEBSITE")
                .rowMapper(new BeanPropertyRowMapper<>(Website.class))
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

    public CommandLineRunner schedulingRunner() {
        return new CommandLineRunner() {
            public void run(String... args) throws Exception {
                try {
                    final TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
                    botsApi.registerBot(telegramPollingBot);

                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        };
    }

}