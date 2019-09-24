package com.jizheping.mapper;

import com.jizheping.api.entity.BidRequestAuditHistory;

public interface BidRequestAuditHistoryMapper {
    //添加审核记录信息
    int saveBidRequestAuditHistory(BidRequestAuditHistory bidRequestAuditHistory);
}
