package com.jizheping.api.util;

import com.alibaba.fastjson.JSON;
import com.jizheping.api.entity.Account;

import java.math.BigDecimal;

/**
 * 序列化与反序列化工具类
 */
public class JsonUtils {
    /**
     * 将java对象转换为json串   序列化
     * @param obj   需要序列化的java对象
     * @param <E>   使用泛型参数接受所有的类
     * @return      返回java对象序列化后的json串(String类型)
     */
    public static <E> String toJson(E obj){
        String json = JSON.toJSONString(obj);

        return json;
    }

    /**
     * 将json串转化为Java对象   反序列化
     * @param json    需要反序列化的json串
     * @param clazz   json反序列化后的类
     * @param <E>     使用泛型接收需要反序列化后的java类
     * @return        返回java类
     */
    public static <E> E toBean(String json,Class<E> clazz){
        E result = JSON.parseObject(json,clazz);

        return result;
    }
}
