package com.jizheping.service;

import com.jizheping.api.entity.BidRequest;
import com.jizheping.api.entity.BidRequestAuditHistory;
import com.jizheping.api.vo.BidRequestAuditQueryObject;
import com.jizheping.api.vo.PageResult;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

public interface BidRequestService {
    public void saveBidRequest(BidRequest bidRequest);

    PageResult selectBidRequestByState(BidRequestAuditQueryObject queryObject);

    void doAuditForPublish(BidRequestAuditHistory bidRequestAuditHistory) throws ParseException;

    BidRequest getBidRequestById(Long id);

    void tender(Long bid, BigDecimal amount,Long userId);

    void doAuditForOne(BidRequestAuditHistory bidRequestAuditHistory) throws ParseException;

    void doAuditForTwo(BidRequestAuditHistory bidRequestAuditHistory) throws ParseException;

    void updateBidRequest(BidRequest bidRequest);
}
