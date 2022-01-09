package com.vakans.bot.job.batch.data;

import lombok.Data;

@Data
public class Vacancy {
    private String title;
    private String description;
    private String company;
    private int minimumSalary;
    private int maximumSalary;
    private String vacancyLink;


    @Override
    public String toString() {
        return "Vacancy{" +
                "title='" + title + '\'' +
                ", company='" + company + '\'' +
                ", minimumSalary=" + minimumSalary +
                ", maximumSalary=" + maximumSalary +
                ", vacancyLink='" + vacancyLink + '\'' +
                '}';
    }
}
