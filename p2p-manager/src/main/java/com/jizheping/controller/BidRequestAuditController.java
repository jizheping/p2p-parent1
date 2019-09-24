package com.jizheping.controller;

import com.jizheping.api.entity.BidRequestAuditHistory;
import com.jizheping.api.entity.LoginInfo;
import com.jizheping.api.util.BidConst;
import com.jizheping.api.vo.BidRequestAuditQueryObject;
import com.jizheping.api.vo.PageResult;
import com.jizheping.api.vo.ResultVO;
import com.jizheping.bidFeign.BidRequestFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/manager")
public class BidRequestAuditController {
    @Autowired
    private BidRequestFeign bidRequestFeign;

    //发表前审核列表分页条件查询
    @RequestMapping("/bidrequest_publishaudit_list")
    public String index(Model model, @ModelAttribute("qo") BidRequestAuditQueryObject bidRequestAuditQueryObject){
        try {
            int[] ids = new int[10];
            ids[0] = BidConst.BIDREQUEST_STATE_PUBLISH_PENDING;
            bidRequestAuditQueryObject.setBidRequestStates(ids);
            bidRequestAuditQueryObject.setState(1);
            LoginInfo loginInfo = LoginInfo.empty(5L);
            loginInfo.setUsername("admin");
            model.addAttribute("logininfo",loginInfo);
            model.addAttribute("qo",bidRequestAuditQueryObject);

            //使用Feign远程调用查询标的信息
            PageResult pageResult = bidRequestFeign.selectBidRequestForPublish(bidRequestAuditQueryObject);

            model.addAttribute("pageResult",pageResult);

            return "bidrequest/publish_audit";
        }catch (Exception e){
            e.printStackTrace();

            return "bidrequest/publish_audit";
        }
    }

    //满标一审分页列表查询
    @RequestMapping("/bidrequest_audit1_list")
    public String audit1(Model model, @ModelAttribute("qo") BidRequestAuditQueryObject bidRequestAuditQueryObject){
        LoginInfo loginInfo = LoginInfo.empty(5L);
        int[] ids = new int[10];
        ids[0] = BidConst.BIDREQUEST_STATE_APPROVE_PENDING_1;
        bidRequestAuditQueryObject.setBidRequestStates(ids);
        bidRequestAuditQueryObject.setState(1);
        loginInfo.setUsername("admin");
        model.addAttribute("logininfo",loginInfo);
        model.addAttribute("qo",bidRequestAuditQueryObject);

        //使用Feign远程调用查询标的信息
        PageResult pageResult = bidRequestFeign.selectBidRequestForPublish(bidRequestAuditQueryObject);

        model.addAttribute("pageResult",pageResult);

        return "bidrequest/audit1";
    }

    //满标二审分页列表查询
    @RequestMapping("/bidrequest_audit2_list")
    public String audit2(Model model, @ModelAttribute("qo") BidRequestAuditQueryObject bidRequestAuditQueryObject){
        LoginInfo loginInfo = LoginInfo.empty(5L);
        int[] ids = new int[10];
        ids[0] = BidConst.BIDREQUEST_STATE_APPROVE_PENDING_2;
        bidRequestAuditQueryObject.setBidRequestStates(ids);
        bidRequestAuditQueryObject.setState(1);
        loginInfo.setUsername("admin");
        model.addAttribute("logininfo",loginInfo);
        model.addAttribute("qo",bidRequestAuditQueryObject);

        //使用Feign远程调用查询标的信息
        PageResult pageResult = bidRequestFeign.selectBidRequestForPublish(bidRequestAuditQueryObject);

        model.addAttribute("pageResult",pageResult);

        return "bidrequest/audit2";
    }

    //发标前审核
    @ResponseBody
    @RequestMapping(value = "/publish_audit_before",method = RequestMethod.POST)
    public ResultVO<Boolean> publishAuditBefore(Long bidRequestId,Integer state,String remark,Long auditId){
        try {
            BidRequestAuditHistory bidRequestAuditHistory = new BidRequestAuditHistory();

            bidRequestAuditHistory.setBidRequestId(bidRequestId);
            bidRequestAuditHistory.setState(state);
            bidRequestAuditHistory.setRemark(remark);
            bidRequestAuditHistory.setAuditor(LoginInfo.empty(auditId));

            bidRequestFeign.publishAuditBefore(bidRequestAuditHistory);

            return ResultVO.success(true);
        }catch (Exception e){
            e.printStackTrace();

            return ResultVO.success(false);
        }
    }

    //满标一审
    @ResponseBody
    @RequestMapping(value = "/publish_audit_one",method = RequestMethod.POST)
    public ResultVO<Boolean> publishAuditOne(Long bidRequestId,Integer state,String remark,Long auditId){
        try {
            if(state == 1){
                BidRequestAuditHistory bidRequestAuditHistory = new BidRequestAuditHistory();

                bidRequestAuditHistory.setBidRequestId(bidRequestId);
                bidRequestAuditHistory.setState(state);
                bidRequestAuditHistory.setRemark(remark);
                bidRequestAuditHistory.setAuditor(LoginInfo.empty(auditId));

                bidRequestFeign.publishAuditOne(bidRequestAuditHistory);
            }else{
                BidRequestAuditHistory bidRequestAuditHistory = new BidRequestAuditHistory();

                bidRequestAuditHistory.setBidRequestId(bidRequestId);
                bidRequestAuditHistory.setState(state);
                bidRequestAuditHistory.setRemark(remark);
                bidRequestAuditHistory.setAuditor(LoginInfo.empty(auditId));

                ResultVO<Boolean> resultVO = bidRequestFeign.publishAuditTwo(bidRequestAuditHistory);

                if(resultVO.getData()){
                    bidRequestFeign.moneyBack(bidRequestId);
                }
            }

            return ResultVO.success(true);
        }catch (Exception e){
            e.printStackTrace();

            return ResultVO.success(false);
        }
    }

    //满标二审
    @ResponseBody
    @RequestMapping(value = "/publish_audit_two",method = RequestMethod.POST)
    public ResultVO<Boolean> publishAuditTwo(Long bidRequestId,Integer state,String remark,Long auditId){
        try {
            if(state == 1){
                BidRequestAuditHistory bidRequestAuditHistory = new BidRequestAuditHistory();

                bidRequestAuditHistory.setBidRequestId(bidRequestId);
                bidRequestAuditHistory.setState(state);
                bidRequestAuditHistory.setRemark(remark);
                bidRequestAuditHistory.setAuditor(LoginInfo.empty(auditId));

                ResultVO<Boolean> resultVO = bidRequestFeign.publishAuditTwo(bidRequestAuditHistory);

                if(resultVO.getData()){
                    bidRequestFeign.loan(bidRequestId);
                }
            }else{
                BidRequestAuditHistory bidRequestAuditHistory = new BidRequestAuditHistory();

                bidRequestAuditHistory.setBidRequestId(bidRequestId);
                bidRequestAuditHistory.setState(state);
                bidRequestAuditHistory.setRemark(remark);
                bidRequestAuditHistory.setAuditor(LoginInfo.empty(auditId));

                ResultVO<Boolean> resultVO = bidRequestFeign.publishAuditTwo(bidRequestAuditHistory);

                if(resultVO.getData()){
                    bidRequestFeign.moneyBack(bidRequestId);
                }
            }

            return ResultVO.success(true);
        }catch (Exception e){
            e.printStackTrace();

            return ResultVO.success(false);
        }
    }
}
