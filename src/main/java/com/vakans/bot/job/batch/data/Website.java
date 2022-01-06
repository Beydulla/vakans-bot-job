package com.vakans.bot.job.batch.data;

import com.vakans.bot.job.batch.data.constants.WebsiteName;
import lombok.Data;

@Data
public class Website {
    private long id;
    private WebsiteName name;
}
