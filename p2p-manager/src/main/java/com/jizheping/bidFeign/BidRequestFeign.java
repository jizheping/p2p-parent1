package com.jizheping.bidFeign;

import com.jizheping.api.entity.BidRequestAuditHistory;
import com.jizheping.api.vo.BidRequestAuditQueryObject;
import com.jizheping.api.vo.PageResult;
import com.jizheping.api.vo.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "p2p-bid-service")
@RequestMapping("/bid")
public interface BidRequestFeign {
    @RequestMapping(value = "/bidrequest_list",method = RequestMethod.POST)
    public PageResult selectBidRequestForPublish(BidRequestAuditQueryObject bidRequestAuditQueryObject);

    @RequestMapping("/publish_audit")
    void publishAuditBefore(BidRequestAuditHistory bidRequestAuditHistory);

    @RequestMapping("/publish_audit_one")
    ResultVO<Boolean> publishAuditOne(BidRequestAuditHistory bidRequestAuditHistory);
    @RequestMapping("/publish_audit_two")
    ResultVO<Boolean> publishAuditTwo(BidRequestAuditHistory bidRequestAuditHistory);

    @RequestMapping("/bid_loan")
    void loan(Long bidRequestId);

    @RequestMapping("/bid_money_back")
    void moneyBack(Long bidRequestId);
}
