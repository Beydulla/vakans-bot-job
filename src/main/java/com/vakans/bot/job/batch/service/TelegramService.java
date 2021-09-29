package com.vakans.bot.job.batch.service;



public interface TelegramService {

    void sendMessage(final long chatId, final String message);

    void handleMessageRequest(final long chatId, final String confirmationKey);

}
