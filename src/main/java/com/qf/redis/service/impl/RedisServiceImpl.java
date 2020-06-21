package com.qf.redis.service.impl;

import com.qf.redis.manager.RedisManager;
import com.qf.redis.service.IRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author YK
 * @version 1.0
 * @date 2020/3/9
 */
@Service
public class RedisServiceImpl implements IRedisService {

    @Autowired
    private RedisManager redisManager;
    @Override
    public void add(String key, String value) {
        redisManager.addKeyAndValue(key,value);
    }

    @Override
    public String getValueForKey(String key) {
        return redisManager.getValueForKey(key);
    }
}
