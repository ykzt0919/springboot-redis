package com.qf.redis.config;

import com.qf.redis.pojo.User;
import com.qf.redis.serializable.YkkSerializable;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author YK
 * @version 1.0
 * @date 2020/4/25
 */
@SpringBootConfiguration
public class RedisConfig {
   private Logger logger= LoggerFactory.getLogger(getClass());

    /**
     * 创建了redis的一个客户端
     * @return
     */
   @Bean
    public RedissonClient redissonClient(){
       RedissonClient redissonClient=null;
       //获取config的实例
       Config config = new Config();
       //设置请求的URL地址
       String url="redis://116.62.179.170:6379";
       //设置config
       config.useSingleServer().setAddress(url);
       //将redis的密码设置到config中
       config.useSingleServer().setPassword("ykzt");
       //通过Redisson来创建一个客户端对象
       try{
           redissonClient= Redisson.create(config);
           logger.info("创建RedissonClient成功");
           return redissonClient;
       }catch (Exception err){
           logger.info("创建RedissonClient失败:"+err.fillInStackTrace());
           return null;
       }
   }


   @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory){

       RedisTemplate<Object,Object> redisTemplate=new RedisTemplate<>();

       //设置连接工厂
       redisTemplate.setConnectionFactory(redisConnectionFactory);

       //设置序列化器  这是key
       redisTemplate.setKeySerializer(new StringRedisSerializer());

       //设置值的序列化器
       //redisTemplate.setValueSerializer(new YkkSerializable(Object.class));

       //设置redis自带的json格式值的序列化器  底层跟我们自定义序列化器的原理一样
       redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<Object>(Object.class));


       return redisTemplate;


   }
}
