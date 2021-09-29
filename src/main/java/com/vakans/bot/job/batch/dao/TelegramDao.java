package com.vakans.bot.job.batch.dao;

public interface TelegramDao {

    int countActiveConfirmationKey(final String confirmationKey);

    int updateByConfirmationKey(final long chatId, final String confirmationKey);
}
