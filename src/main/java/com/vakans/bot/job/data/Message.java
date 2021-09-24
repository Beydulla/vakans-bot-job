package com.vakans.bot.job.data;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Message {

    private long telegramChatId;
    private Vacancy vacancy;
}
