package com.jizheping.api.vo;

/**
 * 请求完成返回时的状态信息
 */

public enum CodeMsg {
    //通用服务
    SUCECC(0,"成功"),
    INTERNAL_ERROR(500,"服务器内部错误"),

    //用户服务
    PWD_ERROR(10001,"密码输入错误"),
    USER_REGISTRY_FALL(10002,"用户名注册失败"),
    USER_NOT_EXISTS(10003,"用户名不存在"),
    USER_SESSION_EXPIRE(10004,"用户为登录或登录时间超时"),

    //账户服务
    ACCOUNT_NOT_EXISTS(200001,"账户不存在"),
    ACCOUNT_CREATE_FALL(200002,"账户创建失败"),

    //借款服务
    BORROW_APPLY_FALL(300001,"用户借款申请失败"),
    BORROW_OPTIMISTIC_LOCK_FAIL(300002,"借款乐观锁错误:%s"),
    BORROW_AUDIT_PUBLISH_FAIL(300003,"发表前审核失败");

    private int code;
    private String msg;

    //将异常消息中的占位符进行替换
    public CodeMsg fillArgs(String... args){
        String newMsg = String.format(this.msg,args);
        this.msg = newMsg;
        return this;
    }

    CodeMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
