package com.vakans.bot.job.batch.data;

import lombok.Data;

import java.util.Arrays;


@Data
public class Filter {

    private long id;
    private String tags;
    private int minimumSalary;
    private int maximumSalary;
    private String company;
    private long telegramChatId;


    public String[] getTrimmedTagsAsArray(){
        final String[] array = tags.split(",");
        Arrays.parallelSetAll(array, (i) -> array[i].trim());
        return array;
    }

}
