package com.jizheping.mapper;

import com.jizheping.api.entity.PaymentScheduleDetail;

import java.util.List;

public interface PaymentScheduleDetailMapper {
    int insert(PaymentScheduleDetail record);

    PaymentScheduleDetail selectByPrimaryKey(Long id);

    List<PaymentScheduleDetail> selectByPaymentSchedule(Long id);

    void updateByPrimaryKey(PaymentScheduleDetail paymentScheduleDetail);

    List<PaymentScheduleDetail> selectByLoginInfoId(Long id);
}
