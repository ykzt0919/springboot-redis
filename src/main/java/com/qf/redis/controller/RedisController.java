package com.qf.redis.controller;

import com.qf.redis.service.IRedisService;
import com.qf.redis.service.impl.TrainTickService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author YK
 * @version 1.0
 * @date 2020/3/6
 */
@RestController
public class RedisController {
    @Autowired
    private IRedisService redisService;

    @Autowired
    private TrainTickService trainTickService;

    @RequestMapping("/addValue")
    public Object add(){
        redisService.add("ykk","来了，老弟");
        return "执行完成。。。。";
    }


    @RequestMapping(value = "/getValue")
    public String getValue(String key){
        return redisService.getValueForKey(key);
    }


    /**
     * 减库存
     * @return
     */
    @RequestMapping("produceStockRedisson")
    public String produceStockRedisson(){
        return trainTickService.produceStockRedisson();
    }
}
