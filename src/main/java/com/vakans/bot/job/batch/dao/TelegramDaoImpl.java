package com.vakans.bot.job.batch.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class TelegramDaoImpl implements TelegramDao{

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Value("${query.count.active.confirmation.key}")
    private String queryCountActiveConfirmationKey;

    @Value("${query.update.confirmation.key}")
    private String queryUpdateConfirmationKey;

    @Override
    public int countActiveConfirmationKey(final String confirmationKey) {
        final Map<String,Object> paramMap = new ConcurrentHashMap<>();
        paramMap.put("CONFIRMATION_KEY", confirmationKey);
        return jdbcTemplate.queryForObject(queryCountActiveConfirmationKey, paramMap, Integer.class);
    }

    @Override
    public int updateByConfirmationKey(final long chatId, final String confirmationKey) {
        final Map<String,Object> paramMap = new ConcurrentHashMap<>();
        paramMap.put("CHAT_ID", chatId);
        paramMap.put("CONFIRMATION_KEY", confirmationKey);
        return jdbcTemplate.update(queryUpdateConfirmationKey, paramMap);
    }
}
