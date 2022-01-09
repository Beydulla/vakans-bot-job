package com.vakans.bot.job.batch.service;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ForceReply;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.vakans.bot.job.batch.dao.TelegramDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class TelegramServiceImpl implements TelegramService{

    private final static Logger LOGGER = LoggerFactory.getLogger(TelegramServiceImpl.class);

    @Value("${telegram.bot.token}")
    private String token;

    private TelegramBot telegramBot;

    @PostConstruct
    public void init(){
        telegramBot = new TelegramBot(token);

    }

    @Override
    public void sendMessage(final long chatId, final String message) {
        LOGGER.info("Sending message to: {}", chatId);
        final SendMessage request = new SendMessage(chatId, message)
                .parseMode(ParseMode.HTML);
        telegramBot.execute(request);
        LOGGER.info("The message has been sent to {}", chatId);
    }

}
