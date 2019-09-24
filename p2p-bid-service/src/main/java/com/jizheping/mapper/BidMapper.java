package com.jizheping.mapper;

import com.jizheping.api.entity.Bid;

import java.util.List;

public interface BidMapper {
    List<Bid> getBidListByBidRequestId(Long id);

    void insertBid(Bid bid);

    List<Bid> getBidListByLoginInfoId(Long id);
}
