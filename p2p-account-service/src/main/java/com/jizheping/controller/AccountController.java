package com.jizheping.controller;

import com.jizheping.api.entity.Account;
import com.jizheping.api.entity.Bid;
import com.jizheping.api.entity.BidRequest;
import com.jizheping.api.entity.PaymentSchedule;
import com.jizheping.api.vo.CodeMsg;
import com.jizheping.api.vo.ResultVO;
import com.jizheping.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.interfaces.ECPrivateKey;
import java.util.List;
import java.util.Map;

/**
 * 账户信息控制层
 */

@RestController
@RequestMapping("/account")
public class AccountController {
    //注入账户信息业务层接口
    @Autowired
    private AccountService accountService;

    //创建一个新的account,添加一条新纪录到account表中
    @RequestMapping("/insertAccount")
    public ResultVO<Account> createAccount(@RequestBody Long id){
        //使用自定义方法获取新的Account对象
        Account account = Account.empty(id);

        //使用try-catch捕获可能产生的异常信息
        try {
            accountService.createAccount(account);

            return ResultVO.success(account);
        }catch (Exception e){
            e.printStackTrace();

            return ResultVO.error(CodeMsg.ACCOUNT_CREATE_FALL);
        }
    }

    /**
     * 根据主键查询帐户的信息
     * @param id   account表主键
     * @return    将查询出来的账户信息进行封装并返回
     */
    @GetMapping("/detail")
    public ResultVO<Account> selectAccount(@RequestParam("userId") Long id){
        //调用业务层方法进行查询
        try {
            Account account = accountService.selectAccountById(id);

            //判断查询出的对象是否为空，判断数据库中是否拥有附和添加的信息
            if(account == null){
                //返回错误信息
                return ResultVO.error(CodeMsg.ACCOUNT_NOT_EXISTS);
            }

            //成功返回
            return ResultVO.success(account);
        }catch (Exception e){
            e.printStackTrace();
            return ResultVO.error(CodeMsg.ACCOUNT_NOT_EXISTS);
        }
    }

    @RequestMapping("/getAccountById")
    public Account getAccountById(@RequestBody Long id){
        Account account = accountService.selectAccountById(id);

        return account;
    }

    @RequestMapping("/updateAccount")
    public int updateAccount(@RequestBody Account account){
        try {
            accountService.updateAccount(account);

            return 1;
        }catch (Exception e){
            e.printStackTrace();

            return 0;
        }
    }

    //修改发标人帐户信息、添加动账记录信息，用于放款
    @RequestMapping("/updateAccountForLoan")
    public void updateAccountForLoan(@RequestBody Account account){
        accountService.updateAccountForLoan(account);
    }

    //修改对应账户信息、添加对应的还款计划、回款计划
    @RequestMapping("/updateAccounts")
    public void updateAccounts(@RequestBody Map<String,String> map){
        try {
            accountService.updateAccounts(map);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RequestMapping("/moneyBack")
    public void moneyBack(@RequestBody Map<String,String> map){
        try {
            accountService.moneyBack(map);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @RequestMapping("/loadRefundList")
    public ResultVO loadRefundList(Long id){
        try {
            List<PaymentSchedule> list = accountService.loadRefundList(id);

            return ResultVO.success(list);
        }catch (Exception e){
            e.printStackTrace();

            return ResultVO.success(null);
        }
    }

    @RequestMapping("/refundMoney")
    public ResultVO<Boolean> refundMoney(Long id){
        try {
            accountService.refundMoney(id);

            return ResultVO.success(true);
        }catch (Exception e){
            e.printStackTrace();

            return ResultVO.success(false);
        }
    }
}
