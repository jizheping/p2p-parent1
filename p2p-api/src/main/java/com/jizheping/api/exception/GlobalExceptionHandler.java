package com.jizheping.api.exception;

import com.jizheping.api.vo.CodeMsg;
import com.jizheping.api.vo.ResultVO;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

//捕获全局异常
@ControllerAdvice
public class GlobalExceptionHandler {

    //返回对象注解
    @ResponseBody
    //统一处理方法抛出的异常
    @ExceptionHandler
    public ResultVO handleException(Exception e){
        /**
         * 判断所产生的异常是否为运行时异常
         * 根据判断返回对应的异常错误信息
         */
        if(e instanceof GlobalException){
            GlobalException g = (GlobalException) e;
            return ResultVO.error(g.getCodeMsg());
        }

        System.out.println("有异常啦");

        //不属于运行时异常时均返回500,内部服务器错误
        return ResultVO.error(CodeMsg.INTERNAL_ERROR);
    }
}
