package com.jizheping.api.vo;

import lombok.Data;

/**
 * 统一返回对象类
 * @param <T>
 */

@Data
public class ResultVO<T> {
    //VO:view object 视图对象
    //POJO:Plan Ordinary Java Object 简单的java对象,实际就是普通javaBean
    //DTO:Data Transfer  数据传输对象,是一种设计模式之间传输数据的软件应用系统

    //状态码
    private int code;
    //错误信息
    private String msg;
    //数据
    private T data;

    public ResultVO(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 处理成功是关注的是数据
     * 处理成功则返回对应的数据
     * @param data
     * @param <T>
     * @return
     */
    public static <T> ResultVO success(T data){
        return new ResultVO(0,null,data);
    }

    /**
     * 处理失败时关注的是失败状态和状态码
     * 处理失败返回对应的错误码和错误信息,方便查看错误原因
     * @param codeMsg
     * @param <T>
     * @return
     */
    public static <T> ResultVO error(CodeMsg codeMsg){
        return new ResultVO(codeMsg.getCode(),codeMsg.getMsg(),null);
    }
}
