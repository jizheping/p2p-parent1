package com.jizheping.mapper;

import com.jizheping.api.entity.PaymentSchedule;

import java.util.List;

public interface PaymentScheduleMapper {
    void insertPaymentSchedule(PaymentSchedule paymentSchedule);

    List<PaymentSchedule> selectPaymentSchduleByUserId(Long id);

    PaymentSchedule selectByPrimaryKey(Long id);

    void updateByPrimaryKey(PaymentSchedule paymentSchedule);
}
