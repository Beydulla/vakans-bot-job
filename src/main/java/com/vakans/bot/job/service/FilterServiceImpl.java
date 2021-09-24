package com.vakans.bot.job.service;

import com.vakans.bot.job.dao.FilterDao;
import com.vakans.bot.job.data.Filter;
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
