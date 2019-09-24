package com.jizheping.controller;

import com.jizheping.api.entity.*;
import com.jizheping.api.util.BidConst;
import com.jizheping.api.vo.BidRequestAuditQueryObject;
import com.jizheping.api.vo.PageResult;
import com.jizheping.api.util.CaculatorUtil;
import com.jizheping.api.vo.CodeMsg;
import com.jizheping.api.vo.ResultVO;
import com.jizheping.service.BidRequestService;
import com.jizheping.service.BidService;
import com.jizheping.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/bid")
public class BidRequestController {
    @Autowired
    private BidRequestService bidRequestService;

    @Autowired
    private BidService bidService;

    @Autowired
    private LoanService loanService;

    //返回对象
    @RequestMapping(value = "/apply1",method = RequestMethod.POST)
    public ResultVO<Boolean> applyBidRequest1(BidRequest bidRequest, LoginInfo loginInfo){
        //添加要还的总利息
        BigDecimal totalRewardAmount = CaculatorUtil.caculateTotalInterest(bidRequest.getBidRequestAmount(),
                bidRequest.getCurrentRate(),bidRequest.getMonthes2Return(),bidRequest.getReturnType());
        bidRequest.setTotalRewardAmount(totalRewardAmount);

        //添加标的创建用户信息(前台处理)
        //bidRequest.setCreateUser(loginInfo);
        //添加提交标的时间
        bidRequest.setApplyDate(new Date());

        return ResultVO.success(true);
    }

    //序列化json
    @RequestMapping(value = "/apply2",method = RequestMethod.POST)
    public ResultVO<Boolean> applyBidRequest2(@RequestBody BidRequest bidRequest){
        System.out.println("apply2    " + bidRequest);

        System.out.println(bidRequest.getCreateUser().getId());

        BigDecimal totalRewardAmount = CaculatorUtil.caculateTotalInterest(bidRequest.getBidRequestAmount(),
                bidRequest.getCurrentRate(),bidRequest.getMonthes2Return(),bidRequest.getReturnType());
        bidRequest.setTotalRewardAmount(totalRewardAmount);

        //添加提交标的时间
        bidRequest.setApplyDate(new Date());

        try{
            bidRequestService.saveBidRequest(bidRequest);

            return ResultVO.success(true);
        }catch (Exception e){
            e.printStackTrace();

            return ResultVO.error(CodeMsg.BORROW_APPLY_FALL);
        }
    }

    //后台分页查询借款表信息
    @RequestMapping(value = "/bidrequest_list",method = RequestMethod.POST)
    public PageResult selectBidRequestForPublish(@RequestBody BidRequestAuditQueryObject bidRequestAuditQueryObject){
        try {
            PageResult pageResult = bidRequestService.selectBidRequestByState(bidRequestAuditQueryObject);

            return pageResult;
        }catch (Exception e){
            e.printStackTrace();

            return null;
        }
    }

    //前台分页查询借款表信息
    @RequestMapping(value = "/bidrequest_list_borrow",method = RequestMethod.POST)
    public ResultVO<PageResult> selectBidRequestForPublishBorrow(@RequestBody BidRequestAuditQueryObject bidRequestAuditQueryObject){
        try {
            PageResult pageResult = bidRequestService.selectBidRequestByState(bidRequestAuditQueryObject);

            return ResultVO.success(pageResult);
        }catch (Exception e){
            e.printStackTrace();

            return null;
        }
    }

    /**
     * 发表前审核
     */
    @ResponseBody
    @RequestMapping(value = "/publish_audit",method = RequestMethod.POST)
    public ResultVO<Boolean> auditForPublish(@RequestBody BidRequestAuditHistory bidRequestAuditHistory){
        try {
            bidRequestService.doAuditForPublish(bidRequestAuditHistory);
        }catch (Exception e){
            e.printStackTrace();
        }

        return ResultVO.success(true);
    }

    //满标一审
    @RequestMapping("/publish_audit_one")
    public ResultVO<Boolean> publishAuditOne(@RequestBody BidRequestAuditHistory bidRequestAuditHistory){
        try {
            bidRequestService.doAuditForOne(bidRequestAuditHistory);

            return ResultVO.success(true);
        }catch (Exception e){
            e.printStackTrace();

            return ResultVO.success(false);
        }
    }

    //满标二审
    @RequestMapping("/publish_audit_two")
    public ResultVO<Boolean> publishAuditTwo(@RequestBody BidRequestAuditHistory bidRequestAuditHistory){
        try {
            bidRequestService.doAuditForTwo(bidRequestAuditHistory);

            return ResultVO.success(true);
        }catch (Exception e){
            e.printStackTrace();

            return ResultVO.success(false);
        }
    }

    //根据id获取标的信息
    @RequestMapping("/getBidRequestById")
    public ResultVO<BidRequest> getBidRequestById(Long id){
        BidRequest bidRequest = bidRequestService.getBidRequestById(id);

        return ResultVO.success(bidRequest);
    }

    //获取投标记录
    @RequestMapping("/getBidList")
    public ResultVO<List> getBidList(Long id){
        try {
            return bidService.getBidList(id);
        }catch (Exception e){
            e.printStackTrace();

            return ResultVO.success(null);
        }
    }

    /**
     * 投标方法
     * @param bid
     * @param amount
     * @return
     */
    @RequestMapping("/tender")
    public ResultVO<Boolean> tender(Long bid,BigDecimal amount,Long userId){
        try {
            bidRequestService.tender(bid,amount,userId);

            return ResultVO.success(true);
        }catch (Exception e){
            e.printStackTrace();

            return ResultVO.success(false);
        }
    }

    //放款
    @RequestMapping("/bid_loan")
    public void loan(@RequestBody Long bidRequestId){
        try {
            loanService.loan(bidRequestId);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //满标二审失败，资金返回
    @RequestMapping("/bid_money_back")
    public void moneyBack(@RequestBody Long bidRequestId){
        try {
            loanService.moneyBack(bidRequestId);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //查询我的投标列表
    @RequestMapping("/getMyBidList")
    public ResultVO getMyBidList(Long id){
        try {
            List<Bid> list = bidService.getBidListByLoginInfoId(id);

            return ResultVO.success(list);
        }catch (Exception e){
            e.printStackTrace();

            return ResultVO.success(null);
        }
    }

    //查询我的借款列表
    @RequestMapping("/loadMyBidRequestList")
    public ResultVO loadMyBidRequestList(Long id){
        try {
            List<BidRequest> list = bidRequestService.getBidRequestListByCreateUserId(id);

            return ResultVO.success(list);
        }catch (Exception e){
            e.printStackTrace();

            return ResultVO.success(null);
        }
    }
}
