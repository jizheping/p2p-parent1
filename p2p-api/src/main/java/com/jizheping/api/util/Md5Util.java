package com.jizheping.api.util;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * md5加密工具类
 */

public class Md5Util {
    /**
     * md5加密方法
     * @param src    需要进行加密的字符串信息
     * @return    返回加密后的字符串信息
     */
    public static String md5(String src){
        return DigestUtils.md5Hex(src);
    }
}
