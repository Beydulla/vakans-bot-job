package com.vakans.bot.job.processor;

import com.vakans.bot.job.data.Filter;
import com.vakans.bot.job.data.Message;
import com.vakans.bot.job.data.Vacancy;
import com.vakans.bot.job.service.FilterService;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FilterVacanciesProcessor implements ItemProcessor<List<Vacancy>, List<Message>> {

    @Autowired
    private FilterService filterService;

    @Override
    public List<Message> process(List<Vacancy> vacancies) throws Exception {
        final List<Filter> filterList = filterService.getFilterList();
        final List<Message> messageList = new ArrayList<>();
        for(final Filter filter : filterList){
            final String[] arrayTag = filter.getTagsAsArray();
            for(final Vacancy vacancy : vacancies){
                if(hasMatch(vacancy, filter, arrayTag)){
                    final Message message = Message.builder()
                            .vacancy(vacancy).telegramChatId(filter.getTelegramChatId()).build();
                    messageList.add(message);

                }
            }
        }
        return messageList;
    }

    private boolean hasMatch(final Vacancy vacancy, final Filter filter, final String[] tags){
        return Arrays.stream(tags).anyMatch(tag ->
                vacancy.getTitle().contains(tag) || vacancy.getDescription().contains(tag))
                && (vacancy.getMinimumSalary() > filter.getMinimumSalary() ||
                vacancy.getMaximumSalary() > filter.getMinimumSalary()) &&
                (vacancy.getCompany() == null || vacancy.getCompany().equalsIgnoreCase(filter.getCompany()));
    }
}
