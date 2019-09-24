package com.jizheping.accountFeign;

import com.jizheping.api.entity.Account;
import com.jizheping.api.entity.Bid;
import com.jizheping.api.entity.BidRequest;
import com.jizheping.api.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@FeignClient(value = "p2p-account-service")
public interface AccountFeign {
    @RequestMapping("/account/updateAccount")
    public int updateAccount(Account account);

    @RequestMapping("/account/getAccountById")
    public Account selectAccount(Long id);

    @RequestMapping("/account/updateAccountForLoan")
    void updateAccountForLoan(Account account);

    @RequestMapping("/account/updateAccounts")
    void updateAccounts(Map<String,String> map);

    @RequestMapping("/account/moneyBack")
    void moneyBack(Map<String, String> map);
}
