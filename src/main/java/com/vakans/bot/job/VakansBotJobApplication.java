package com.vakans.bot.job;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

@SpringBootApplication
@EnableScheduling
public class VakansBotJobApplication {

	public static void main(String[] args) throws IOException {
		OutputStream output = new FileOutputStream("application.properties");
		Properties properties = new Properties();
		properties.setProperty("aasfd", "bbbb");
		properties.store(output, null);


		SpringApplication.run(VakansBotJobApplication.class, args);
	}

}
