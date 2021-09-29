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
import java.util.UUID;

@Service
public class TelegramServiceImpl implements TelegramService{

    private final static Logger LOGGER = LoggerFactory.getLogger(TelegramServiceImpl.class);

    @Value("${telegram.bot.token}")
    private String token;

    @Autowired
    private TelegramDao telegramDao;
    private TelegramBot telegramBot;

    @PostConstruct
    public void init(){
        telegramBot = new TelegramBot(token);

    }

    @Override
    public void sendMessage(final long chatId, final String message) {
        final SendMessage request = new SendMessage(chatId, message)
                .parseMode(ParseMode.HTML);
        telegramBot.execute(request);
    }

    private boolean isConfirmationKeyExist(final String confirmationKey) {
        return telegramDao.countActiveConfirmationKey(confirmationKey) != 0;
    }

    @Override
    public void handleMessageRequest(final long chatId, final String messageText) {
        LOGGER.info("Validating messageText, {}", messageText);
        try {
            if(UUID.fromString(messageText).toString().equals(messageText) && isConfirmationKeyExist(messageText)){
                telegramDao.updateByConfirmationKey(chatId, messageText);
                sendMessage(chatId, "Sorğunuz təsdiq edildi!");
            }else{
                LOGGER.info("message text is not confirmation key: {}", messageText);
                sendMessage(chatId, "Göndərdiyiniz kod düzgün deyil. " +
                        "\nXahiş edirik, saytdan yeni kod əldə edəsiniz!");
            }
        }catch (IllegalArgumentException illegalArgumentException){
            LOGGER.info("message text is not confirmation key: {}", messageText);
            sendMessage(chatId, "Göndərdiyiniz kod düzgün deyil. " +
                    "\nXahiş edirik, saytdan yeni kod əldə edəsiniz!");
        }
    }
}
