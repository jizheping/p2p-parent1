package com.jizheping.service.impl;

import com.jizheping.api.entity.Bid;
import com.jizheping.api.vo.ResultVO;
import com.jizheping.mapper.BidMapper;
import com.jizheping.service.BidService;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BidServiceImpl implements BidService {
    @Autowired
    private BidMapper bidMapper;

    @Override
    public ResultVO<List> getBidList(Long id) {
        List<Bid> list = bidMapper.getBidListByBidRequestId(id);

        return ResultVO.success(list);
    }

    @Override
    public List<Bid> getBidListByLoginInfoId(Long id) {
        return bidMapper.getBidListByLoginInfoId(id);
    }
}
