package com.vakans.bot.job.batch.dao;

import com.vakans.bot.job.batch.data.Filter;

import java.util.List;

public interface FilterDao {

    List<Filter> getFilterList();
}
