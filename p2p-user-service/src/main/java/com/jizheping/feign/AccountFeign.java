package com.jizheping.feign;

import com.jizheping.api.entity.Account;
import com.jizheping.api.vo.ResultVO;
import com.jizheping.feign.fallback.AccountFeignFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "p2p-account-service",fallback = AccountFeignFallback.class)
public interface AccountFeign {
    @RequestMapping("/account/insertAccount")
    public ResultVO<Account> createAccount(Long id);
}
