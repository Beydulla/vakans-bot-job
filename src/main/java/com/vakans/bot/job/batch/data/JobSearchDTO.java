package com.vakans.bot.job.batch.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class JobSearchDTO {
    private long id;
    private String title;
    private String slug;
}
