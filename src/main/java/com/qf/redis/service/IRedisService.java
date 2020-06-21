package com.qf.redis.service;

/**
 * @author YK
 * @version 1.0
 * @date 2020/3/9
 */
public interface IRedisService {
    /**
     * 添加数据到Redis中
     * @param key
     * @param value
     */
    void add(String key,String value);

    /**
     * 通过key获取值
     * @param key
     * @return
     */
    String getValueForKey(String key);

}
