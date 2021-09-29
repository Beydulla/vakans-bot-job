package com.vakans.bot.job.batch.service;

import com.vakans.bot.job.batch.dao.FilterDao;
import com.vakans.bot.job.batch.data.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FilterServiceImpl implements FilterService{

    @Autowired
    private FilterDao filterDao;

    @Override
    public List<Filter> getFilterList() {
        return filterDao.getFilterList();
    }
}
