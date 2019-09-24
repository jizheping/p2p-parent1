package com.jizheping.api.util;

import ch.qos.logback.core.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * redis工具类
 */

@Component
public class RedisUtil {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 判断键为key的Redis存储是否存在
     * @param key   Redis中的key
     * @return
     */
    public boolean exists(String key){
        return stringRedisTemplate.hasKey(key);
    }

    /**
     * 向Redis中添加有效时间的新缓存
     * @param key         添加缓存的键
     * @param value       添加缓存的值
     * @param secounds    缓存的过期时间,单位:秒
     * @param <T>
     */
    public <T> void setex(String key,T value,int secounds){
        String json = toJson(value);

        stringRedisTemplate.opsForValue().set(key,json,secounds, TimeUnit.SECONDS);
    }

    /**
     * 向Redis缓存中添加新缓存
     * @param key      添加缓存的键
     * @param value    添加缓存的值
     * @param <T>       使用泛型参数，需要添加到Redis缓存的java对象
     * @return          无返回
     */
    public <T> void get(String key,T value){
        String json = toJson(value);
        stringRedisTemplate.opsForValue().set(key,json);
    }

    /**
     * 从Redis中获取对应key的值
     * @param key       缓存中的键
     * @param clazz     获取缓存的对象
     * @param <T>
     * @return
     */
    public <T> T get(String key,Class<T> clazz){
        String json = stringRedisTemplate.opsForValue().get(key);

        if(json == null){
            System.out.println("Redis中不存此缓存");

            return null;
        }

        T result = toBean(json,clazz);

        return result;
    }

    /**
     * 将json转化为java对象
     * @param value    json串
     * @param clazz    转换后的java对象
     * @param <T>
     * @return
     */
    private <T> T toBean(String value,Class<T> clazz){
        T result = null;
        if(clazz == String.class){
            result = (T) value;
        }else if(clazz == int.class || clazz == Integer.class){
            result = (T) Integer.valueOf(value);
        }else if(clazz == Long.class || clazz == long.class){
            result = (T) Long.valueOf(value);
        }else{
            result = JsonUtils.toBean(value,clazz);
        }

        return result;
    }

    /**
     * 将java对象转换为json串
     * @param value    需要转换为json的java对象
     * @param <T>
     * @return
     */
    private <T> String toJson(T value){
        String result = "";
        Class<?> clazz = value.getClass();

        if(clazz == String.class){
            result = (String) value;
        }else if(clazz == int.class || clazz == Integer.class || clazz == Long.class || clazz == long.class){
            result = String.valueOf(value);
        }else{
            result = JsonUtils.toJson(value);
        }

        return result;
    }
}
