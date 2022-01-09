package com.vakans.bot.job.batch.processor;

import com.vakans.bot.job.batch.data.Filter;
import com.vakans.bot.job.batch.data.Message;
import com.vakans.bot.job.batch.data.Vacancy;
import com.vakans.bot.job.batch.service.FilterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FilterVacanciesProcessor implements ItemProcessor<List<Vacancy>, List<Message>> {

    private final static Logger LOGGER = LoggerFactory.getLogger(FilterVacanciesProcessor.class);


    @Autowired
    private FilterService filterService;

    @Override
    public List<Message> process(List<Vacancy> vacancies){
        LOGGER.info("Getting list of filters");
        final List<Filter> filterList = filterService.getFilterList();
        LOGGER.info("{} filters fetched", filterList.size());
        final List<Message> messageList = new ArrayList<>();
        for(final Filter filter : filterList){
            LOGGER.info("Checking match with filter: {}", filter.toString());
            final String[] arrayTag = filter.getTrimmedTagsAsArray();
            for(final Vacancy vacancy : vacancies){
                if(hasMatch(vacancy, filter, arrayTag)){
                    final Message message = Message.builder()
                            .vacancy(vacancy).telegramChatId(filter.getTelegramChatId()).build();
                    messageList.add(message);

                }
            }
        }
        LOGGER.info("{} messages generated", messageList.size());
        return messageList;
    }

    private boolean hasMatch(final Vacancy vacancy, final Filter filter, final String[] tags){
        return Arrays.stream(tags).anyMatch(tag ->
                vacancy.getTitle().toLowerCase().contains(tag.toLowerCase())
                || (vacancy.getDescription() != null && vacancy.getDescription().toLowerCase().contains(tag.toLowerCase())))
                && ((vacancy.getMinimumSalary() == 0 || vacancy.getMinimumSalary() >= filter.getMinimumSalary()) ||
                (vacancy.getMaximumSalary() == 0 || vacancy.getMaximumSalary() >= filter.getMinimumSalary())) &&
                (filter.getCompany() == null || filter.getCompany().isEmpty()
                        || vacancy.getCompany().equalsIgnoreCase(filter.getCompany()));
    }
}
