package com.vakans.bot.job.batch.data;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Message {

    private long telegramChatId;
    private Vacancy vacancy;
}
