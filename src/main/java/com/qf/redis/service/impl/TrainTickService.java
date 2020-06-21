package com.qf.redis.service.impl;

import com.qf.redis.lock.DistributeRedisLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author YK
 * @version 1.0
 * @date 2020/3/9
 * 买火车票
 */
@Service
public class TrainTickService {
    private Logger logger= LoggerFactory.getLogger(getClass());

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private DistributeRedisLock distributeRedisLock;

    /**
     * 卖火车票
     * 将所有的票放到redis缓存中
     * 场景卖火车票的系统 假设1000个人抢100张票
     *
     */
    public  String produceStock(){
        //设置分布式锁
        String lock="lock";
        String value= UUID.randomUUID().toString();
        try {
//            //setnx命令
//            Boolean tag = stringRedisTemplate.opsForValue().setIfAbsent(lock, "");
//            //给可以设置过期时间
//            //执行这句话是突然死机了 就会出现死锁
//            stringRedisTemplate.expire(lock,60, TimeUnit.SECONDS);
            //在高版本的redis中是可以将以上setnx和exprie一起设置保证一致性
            //底层是redis和lula脚本 在c的层面上实现原子性的
            Boolean tag = stringRedisTemplate.opsForValue().setIfAbsent(lock, value, 30, TimeUnit.SECONDS);
            if (!tag){
                return "当前排队人数过多请等待";
            }

            /**
             *场景第一个人的程序还没有执行完锁已经过期   第二人进来创建了锁  第一个人执行完了将第二个锁删除了
             *
             * 解决比较key中的value值相同才删除 不同就等待锁过期
             *
             * 预防锁过期程序还没执行完   开启一个守护线程  每隔锁过期时间的1/3的时间给锁续命
             *
             */
            //开启守护线程保证锁能完全锁住票  保证程序的正常执行
            //传入锁
            myThred myThred = new myThred(lock);
            myThred.setDaemon(true);//设置成后台线程
            myThred.start();//运行  与整个方法同生死
            //在redis中获取票的剩余量
            int stock =  Integer.parseInt(stringRedisTemplate.opsForValue().get("traintickes"));
            //判断剩余票量是否能继续卖
            if (stock>0){//说明可以减去库存
                int rStock=stock-1;
                //将真实的库存放入带redis中
                stringRedisTemplate.opsForValue().set("traintickes",String.valueOf(rStock));
                logger.info("购票成功");
            }else {
                logger.info("购票失败库存不足");
            }
        }finally {//执行完毕之后将锁删除
            if (value.equals(stringRedisTemplate.opsForValue().get(lock))) {//将设置的值与获取的值做对比相同删除锁
                //不同就等待锁过期
                stringRedisTemplate.delete(lock);
            }
        }

        return "抢票成功";
    }


    /**
     * 使用后台线程进行续命
     * 守护线程
     *   在主程序下 开启一个守护线程  守护线程的生命周期与主线程同生死
     */
    class myThred extends Thread{
        //利用构造方法将锁传入
        String lock;
        public myThred(String lock){
            this.lock=lock;
        }

        @Override
        public void run() {
            //需要重复执行  守护线程的生命周期不受自己控制
            while (true){
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //假设主程序还活着说明要继续续命
                stringRedisTemplate.expire(lock,30,TimeUnit.SECONDS);
            }

        }
    }


    /**
     * 使用redisson来完成
     * @return
     */
    public String produceStockRedisson(){
        String lock="lock";
        try {
            boolean lock1 = distributeRedisLock.lock(lock);
            System.out.println(lock1);
            if(true==lock1){//说明加锁成功
                int stock = Integer.parseInt(stringRedisTemplate.opsForValue().get("traintickes"));
                //首先要判断下 这个库存是否>0
                if (stock > 0) {  //说明可以减去库存
                    int rStock = stock - 1;
                    //下一步：将真实的库存放到咋们的Redis中去
                    stringRedisTemplate.opsForValue().set("traintickes", String.valueOf(rStock));
                    logger.info("扣减库存成功....剩余库存:" + rStock);

                } else {   //说明不能扣减库存
                    logger.info("库存扣减失败、库存是负数、不足...");
                }  //已经用了15秒钟
            }else{
                return "当前的排队人数过多...";
            }
        }finally {
            distributeRedisLock.unlock(lock);
        }
        return "抢票成功....";

    }

}
