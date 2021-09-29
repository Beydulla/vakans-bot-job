package com.vakans.bot.job;

import com.vakans.bot.job.polling.TelegramBot;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
@EnableScheduling
@EnableBatchProcessing
public class VakansBotJobApplication {

	public static void main(String[] args) {
		SpringApplication.run(VakansBotJobApplication.class, args);
	}

}
