package com.qf.redis.lock;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author YK
 * @version 1.0
 * @date 2020/3/9
 * 用来加锁和解锁的
 */
@Component//注入ioc容器中
public class DistributeRedisLock {

    private Logger logger= LoggerFactory.getLogger(getClass());

    @Autowired
    private RedissonClient redissonClient;


    //一个方法用来加锁

    /**
     * 加锁成功....
     * @param lockName
     * @return
     */
    public boolean lock(String lockName){
        try {
            if(null==redissonClient){  //如果对象没有注入进来那么说明是有问题的
                logger.info("注入redissonClient对象失败....");
                return false;
            }
            //获取这个锁
            RLock lock = redissonClient.getLock(lockName);
            //锁住了
            lock.lock(30, TimeUnit.SECONDS);
            logger.info("加锁成功.......");
            return true;
        } catch (Exception e) {
            logger.info("不可预期的异常造成了加锁失败....");
            return false;
        }
    }


    /**
     * 释放锁
     * @param lockName
     * @return
     */
    public boolean unlock(String lockName){
        try {
            if(null==redissonClient){  //说明没法释放出问题了....
                logger.info("释放锁失败----"+lockName);
            }
            //获取到这个锁对象
            RLock lock = redissonClient.getLock(lockName);
            if(null!=lock){
                lock.unlock();
                logger.info("释放锁成功....");
                return true;
            }
            return false;
        } catch (Exception e) {
            logger.info("释放锁失败了....");
            return false;
        }
    }
}
