package com.jizheping.api.exception;

import com.jizheping.api.vo.CodeMsg;
import lombok.Data;

@Data
public class GlobalException extends RuntimeException{
    //错误信息对象
    private CodeMsg codeMsg;

    public GlobalException(CodeMsg codeMsg){
        this.codeMsg = codeMsg;
    }
}
