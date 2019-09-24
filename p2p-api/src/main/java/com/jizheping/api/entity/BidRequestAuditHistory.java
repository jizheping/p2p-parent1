package com.jizheping.api.entity;

import com.jizheping.api.util.BidConst;
import lombok.Data;

/**
 * 标的审核信息实体类
 */

@Data
public class BidRequestAuditHistory extends BaseAuditDomain{
    private Long bidRequestId;
    //审核状态
    private int auditType = BidConst.PUBLISH_AUDIT;
}
