package com.vakans.bot.job.data;

import lombok.Data;



@Data
public class Filter {

    private long id;
    private String tags;
    private int minimumSalary;
    private int maximumSalary;
    private String company;
    private long telegramChatId;


    public String[] getTagsAsArray(){
        return tags.split(",");
    }

}
