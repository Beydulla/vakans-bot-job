package com.vakans.bot.job.polling;

import com.vakans.bot.job.batch.service.TelegramService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;


@Component
public class TelegramPollingBot extends TelegramLongPollingBot {

    private final static Logger LOGGER = LoggerFactory.getLogger(TelegramPollingBot.class);

    @Autowired
    private TelegramService telegramService;

    @Value("${telegram.bot.username}")
    private String botUsername;
    @Value("${telegram.bot.token}")
    private String botToken;


    @Override
    public void onUpdateReceived(Update update) {
        final String messageText = update.getMessage().getText();
        LOGGER.info("Received message text; {}", messageText);
        telegramService.handleMessageRequest(update.getMessage().getChatId(), messageText);
        LOGGER.info("Finished handling request");
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}