package com.jizheping.service;

import com.jizheping.api.entity.PaymentSchedule;
import com.jizheping.api.vo.ResultVO;

import java.util.List;

public interface BidService {
    ResultVO<List> getBidList(Long id);

}
