package com.vakans.bot.job.batch.data;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class JobSearchDTO {
    private long id;
    private String title;
    private String slug;
    @JsonAlias("created_at")
    private String createdAt;
    private Map<String, String> company;
}
