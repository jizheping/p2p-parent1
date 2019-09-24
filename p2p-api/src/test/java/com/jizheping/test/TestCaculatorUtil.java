package com.jizheping.test;

import com.jizheping.api.util.BidConst;
import com.jizheping.api.util.CaculatorUtil;

import java.math.BigDecimal;

public class TestCaculatorUtil {
    public static void main(String[] args) {
        BigDecimal totalAmount = CaculatorUtil.caculateTotalInterest(BigDecimal.valueOf(120000),BigDecimal.valueOf(6),12, BidConst.RETURN_TYPE_MONTH_PRINCIPAL);

        System.out.println("总利息为:"+totalAmount);
    }
}
