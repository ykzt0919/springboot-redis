package com.qf.redis.serializable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

/**
 * @author YK
 * @version 1.0
 * @date 2020/4/26
 * 自定义序列化器
 * 就是定义  放入redis数据库中的key和value以什么形式存放
 */

public class YkkSerializable implements RedisSerializer {
    //利用构造器将类对象传入自定义序列化器中
    private Class clazz;
    public YkkSerializable(Class clazz){
        this.clazz=clazz;
    }

    /**
     * 序列化
     * 就是将对象转换成字符串的方法
     * @param o
     * @return
     * @throws SerializationException
     */
    @Override
    public byte[] serialize(Object o) throws SerializationException {
        if(o==null){//如果接收到的对象为空直接返回null不作处理
            return null;
        }
        //将接收到的对象转换为json对象
        String jsonString= JSON.toJSONString(o);
        return jsonString.getBytes(Charset.forName("utf-8"));//返回json对象并设置成utf-8编码

    }

    /**
     * 反序列化将redis数据库中的字符串数据转换成java对象
     * @param bytes
     * @return
     * @throws SerializationException
     */
    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        if(bytes==null){
            return null;
        }
        //将string类型的json转换成java对象
        String Result=new String(bytes);
        return JSON.parseObject(Result,clazz);

    }
}
