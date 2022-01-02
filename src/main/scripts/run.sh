#!/bin/bash
nohup java -cp ./lib/*:./resources/ -Dlogging.config=./resources/logback-spring.xml com.vakans.bot.job.VakansBotJobApplication
