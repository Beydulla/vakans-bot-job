package com.vakans.bot.job;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class VakansBotJobApplication {

	public static void main(String[] args){
		SpringApplication.run(VakansBotJobApplication.class, args);
	}

}
