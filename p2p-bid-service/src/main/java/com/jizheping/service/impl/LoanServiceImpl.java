package com.jizheping.service.impl;

import com.jizheping.accountFeign.AccountFeign;
import com.jizheping.api.entity.Account;
import com.jizheping.api.entity.Bid;
import com.jizheping.api.entity.BidRequest;
import com.jizheping.api.util.JsonUtils;
import com.jizheping.mapper.BidMapper;
import com.jizheping.mapper.BidRequestMapper;
import com.jizheping.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LoanServiceImpl implements LoanService {
    @Autowired
    private BidRequestMapper bidRequestMapper;

    @Autowired
    private BidMapper bidMapper;

    @Autowired
    private AccountFeign accountFeign;

    //满标二审通过，进行放款
    @Override
    public void loan(Long bidRequestId) {
        //根据标的id查询对应的标的信息
        BidRequest bidRequest = bidRequestMapper.selectBidRequestById(bidRequestId);
        //获取发表人id
        Long borrower = bidRequest.getCreateUser().getId();
        //获取还款期数
        int returnDays = bidRequest.getMonthes2Return();
        //获取借款金额
        BigDecimal amount = bidRequest.getBidRequestAmount();
        //获取总利息
        BigDecimal totalRewardAmount = bidRequest.getTotalRewardAmount();
        //获取待还本息
        BigDecimal unReturnAmount = amount.add(totalRewardAmount);
        //创建account对象，用于Feign调用传参
        Account account = new Account();
        account.setId(borrower);
        account.setUsableAmount(amount);
        account.setUnReturnAmount(unReturnAmount);
        account.setRemainBorrowLimit(amount);
        //使用Feign修改标的发布人的账户信息，添加对应的动账信息
        accountFeign.updateAccountForLoan(account);
        //获取投标记录列表
        List<Bid> bidList = bidMapper.getBidListByBidRequestId(bidRequestId);

        List<String> bidJsonList = new ArrayList<>();

        BigDecimal amountSum = BigDecimal.ZERO;

        for(Bid bid : bidList){
            bidJsonList.add(JsonUtils.toJson(bid));
        }

        Map<String,String> map = new HashMap<>();

        map.put("bidRequest",JsonUtils.toJson(bidRequest));
        map.put("bidJsonList",JsonUtils.toJson(bidJsonList));

        //通过Feign调用修改对应账户信息、添加对应的还款计划、回款计划
        accountFeign.updateAccounts(map);
    }

    //满标二审失败，资金返回
    @Override
    public void moneyBack(Long bidRequestId) {
        //根据标的id查询对应的标的信息
        BidRequest bidRequest = bidRequestMapper.selectBidRequestById(bidRequestId);
        //获取投标记录列表
        List<Bid> bidList = bidMapper.getBidListByBidRequestId(bidRequestId);

        List<String> bidJsonList = new ArrayList<>();

        BigDecimal amountSum = BigDecimal.ZERO;

        for(Bid bid : bidList){
            bidJsonList.add(JsonUtils.toJson(bid));
        }

        Map<String,String> map = new HashMap<>();

        map.put("bidRequest",JsonUtils.toJson(bidRequest));
        map.put("bidJsonList",JsonUtils.toJson(bidJsonList));

        //通过Feign调用修改对应账户信息、添加对应的还款计划、回款计划
        accountFeign.moneyBack(map);
    }
}
