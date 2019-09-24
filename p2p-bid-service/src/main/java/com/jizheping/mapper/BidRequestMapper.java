package com.jizheping.mapper;

import com.jizheping.api.entity.BidRequest;
import com.jizheping.api.vo.BidRequestAuditQueryObject;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BidRequestMapper {
    void addBitRequest(BidRequest bidRequest);

    //根据条件查询借款标
    List<BidRequest> selectBidRequestByCondition(@Param("qo") BidRequestAuditQueryObject queryObject);

    int updateBidRequest(BidRequest bidRequest);

    BidRequest selectBidRequestById(Long bidRequestId);
}
