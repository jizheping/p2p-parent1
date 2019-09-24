package com.jizheping.service;

import com.jizheping.api.entity.*;

import java.util.List;
import java.util.Map;

public interface AccountService {
    /**
     * 添加一条新的账户信息
     * @param account   控制层使用try-catch捕获异常信息，所以无返回
     */
    void createAccount(Account account);

    /**
     * 根据主键查询对应的账户信息
     * @param id   主键Id
     * @return    返回对应的账户信息
     */
    Account selectAccountById(Long id);

    void updateAccount(Account account);

    void updateAccountForLoan(Account account);

    //修改对应账户信息、添加对应的还款计划、回款计划
    void updateAccounts(Map<String,String> map);

    //二审失败，资金返回
    void moneyBack(Map<String, String> map);

    //加载还款计划列表
    List<PaymentSchedule> loadRefundList(Long id);

    //还款
    void refundMoney(Long id);

    //加载回款计划列表
    List<PaymentScheduleDetail> loadReturnList(Long id);
}
