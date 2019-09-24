package com.jizheping.api.util;

/**
 * 用户位状态信息工具类
 */

public class BitStatesUtils {
    //用户注册后的默认标识
    public static final Long OP_BASIC_INFO = 1L;
    //用户绑定手机状态码
    public static final Long OP_BIND_PHONE = 2L << 0;
    //用户绑定邮箱状态码
    public static final Long OP_BIND_EMAIL = 2L << 1;
    //用户基本资料填写
    public static final Long OP_BASE_INFO = 2L << 2;
    //用户实名认证状态码
    public static final Long OP_REAL_AUTH = 2L << 3;
    //视频认证状态码
    public static final Long OP_VEDIO_AUTH = 2L << 4;
    //是否拥有借款状态码
    public static final Long OP_HAS_BIDREQUEST = 2L << 5;

    /**
     * 判断是否具有当前状态
     * 使用位与运算符,都为1则为1,否则为0
     * @param oldState    当前数据库中的状态码信息
     * @param state       判断是否拥有的状态码
     * @return            返回boolean
     */
    public static boolean hasState(Long oldState,Long state){
        return (oldState & state) != 0;
    }

    /**
     * 向数据库中的状态码信息添加当前状态码信息
     * 使用位或运算,一个为1则为1
     * @param oldState      数据库中的状态码信息
     * @param state         需要添加的状态码信息
     * @return               返回添加后的状态码信息
     */
    public static long addState(Long oldState,Long state){
        if(hasState(oldState,state)){
            return oldState;
        }

        return (oldState | state);
    }

    /**
     * 删除数据库中状态码中的某一状态信息
     * 使用与非运算,两个为1则为0
     * @param oldState        数据库中的状态码信息
     * @param state            要移除的状态码信息
     * @return                  移除后的状态码信息
     */
    public static long removeState(Long oldState,Long state){
        if(hasState(oldState,state)){
            return oldState;
        }

        return (oldState ^ state);
    }
}
