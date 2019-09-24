package com.jizheping.service;

public interface LoanService {
    void loan(Long bidRequestId);

    void moneyBack(Long bidRequestId);
}
