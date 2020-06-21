package com.qf.redis.manager;

import com.qf.redis.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author YK
 * @version 1.0
 * @date 2020/3/9
 * 这里做的是业务下沉  主要是写所有访问redis库
 */
@Component //将这个类放入IOC容器
public class RedisManager {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 向redis存放键对
     * @param key
     * @param value
     */
    public void addKeyAndValue(String key,String value){
        /**
         * stringRedisTemplate.opsForValue(); 这个就是用来操作 String类型的
         * stringRedisTemplate.opsForList(); 这个主要就用来操作list的
         * stringRedisTemplate.opsForZSet(); 这个主要用来操作sorted set
         * stringRedisTemplate.opsForHash(); 用来操作hash结构的
         * stringRedisTemplate.opsForSet();  这个就是用来操作Set数据类型的
         */
        redisTemplate.opsForValue().set("user", new User("ykk","123"));
       // stringRedisTemplate.opsForValue().set(key,value);
    }

    /**
     * 通过key在redis中取值
     * @param key
     * @return
     */
    public String getValueForKey(String key){

//        stringRedisTemplate.expire("nz1904",60, TimeUnit.SECONDS);
//        stringRedisTemplate.delete("nz1904");
//        stringRedisTemplate.hasKey("nz1904");
//        stringRedisTemplate.getExpire("nz1904");
//        stringRedisTemplate.opsForValue().increment("nz1904",-100);
//        stringRedisTemplate.opsForHash().entries("一个或多个key");
//        stringRedisTemplate.opsForHash().put("集合的名字","key值","value值");
       return stringRedisTemplate.opsForValue().get(key);
    }
    /**
     * 常用的api
     * //相当于expire  key 过期时间
     * stringRedisTemplate.expire("nz1904",60, TimeUnit.SECONDS);
     * //相当于del key删除某个键
     * stringRedisTemplate.delete("nz1904");
     * //相当于 exists key判断某个键是否存在
     * stringRedisTemplate.hasKey("nz1904");
     * //相当于ttl key获取当前键的过期时间
     * stringRedisTemplate.getExpire("nz1904");
     * //相当于incrby key 增长的长度
     *stringRedisTemplate.opsForValue().increment("nz1904",100);
     *  //相当于decrby key 减少的长度
     *stringRedisTemplate.opsForValue().increment("nz1904",-100);
     *   //相当于hmget
     *   stringRedisTemplate.opsForHash().entries("一个或多个key");
     *   //相当于hmset
     *  stringRedisTemplate.opsForHash().putAll("key", );
     *  //相当于hset 集合的决名字 键 值
     *stringRedisTemplate.opsForHash().put("集合的名字","key值","value值");
     *
     *
     */
}
