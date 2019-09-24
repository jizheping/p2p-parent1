package com.jizheping.feign.fallback;

import com.jizheping.api.vo.CodeMsg;
import com.jizheping.api.vo.ResultVO;
import com.jizheping.feign.AccountFeign;
import org.springframework.stereotype.Component;

@Component
public class AccountFeignFallback implements AccountFeign {
    @Override
    public ResultVO createAccount(Long id) {
        System.out.println("account-service.insertAccount: fallback");

        return ResultVO.error(CodeMsg.ACCOUNT_CREATE_FALL);
    }
}
