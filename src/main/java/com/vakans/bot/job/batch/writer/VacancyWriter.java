package com.vakans.bot.job.batch.writer;

import com.vakans.bot.job.batch.data.Message;
import com.vakans.bot.job.batch.data.Vacancy;
import com.vakans.bot.job.batch.service.TelegramService;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class VacancyWriter implements ItemWriter<List<Message>> {

    @Autowired
    private TelegramService telegramService;

    @Override
    public void write(List<? extends List<Message>> messageList) throws Exception {
        for(final List<Message> messages : messageList){
            for(final Message message : messages){
                telegramService.sendMessage(message.getTelegramChatId(), generateMessage(message.getVacancy()));
            }
        }
    }


    private String generateMessage(final Vacancy vacancy){
        return "Title: " + vacancy.getTitle() + "\n" +
            "Company: " + vacancy.getCompany() + "\n" +
            "Salary: " + vacancy.getMinimumSalary() + " - " + vacancy.getMaximumSalary() + "\n" +
            "Link: " + vacancy.getVacancyLink();
    }
}
