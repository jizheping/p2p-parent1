package com.jizheping.api.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jizheping.api.util.BidConst;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 回款计划
 */

public class PaymentScheduleDetail {
    private Long id;
    private BigDecimal bidAmount; // 该投标人总共投标金额,便于还款/垫付查询
    private Long bidId; // 对应的投标ID
    private BigDecimal totalAmount = BidConst.ZERO; // 本期还款总金额(=本金+利息)
    private BigDecimal principal = BidConst.ZERO; // 本期应还款本金
    private BigDecimal interest = BidConst.ZERO; // 本期应还款利息
    private int monthIndex; // 第几期（即第几个月）
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:SS")
    private Date deadline; // 本期还款截止时间
    private Long bidRequestId; // 所属哪个借款
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:SS")
    private Date payDate; // 实际付款日期
    private int returnType; // 还款方式
    private Long paymentScheduleId; // 所属还款计划
    private LoginInfo fromLogininfo; // 还款人(即发标人)
    private Long toLogininfoId; // 收款人(即投标人)

    public BigDecimal getBidAmount() {
        return bidAmount;
    }
    public void setBidAmount(BigDecimal bidAmount) {
        this.bidAmount = bidAmount;
    }
    public Long getBidId() {
        return bidId;
    }
    public void setBidId(Long bidId) {
        this.bidId = bidId;
    }
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
    public BigDecimal getPrincipal() {
        return principal;
    }
    public void setPrincipal(BigDecimal principal) {
        this.principal = principal;
    }
    public BigDecimal getInterest() {
        return interest;
    }
    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }
    public int getMonthIndex() {
        return monthIndex;
    }
    public void setMonthIndex(int monthIndex) {
        this.monthIndex = monthIndex;
    }
    public Date getDeadline() {
        return deadline;
    }
    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }
    public Long getBidRequestId() {
        return bidRequestId;
    }
    public void setBidRequestId(Long bidRequestId) {
        this.bidRequestId = bidRequestId;
    }
    public Date getPayDate() {
        return payDate;
    }
    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }
    public int getReturnType() {
        return returnType;
    }
    public void setReturnType(int returnType) {
        this.returnType = returnType;
    }
    public Long getPaymentScheduleId() {
        return paymentScheduleId;
    }
    public void setPaymentScheduleId(Long paymentScheduleId) {
        this.paymentScheduleId = paymentScheduleId;
    }
    public LoginInfo getFromLogininfo() {
        return fromLogininfo;
    }
    public void setFromLogininfo(LoginInfo fromLogininfo) {
        this.fromLogininfo = fromLogininfo;
    }
    public Long getToLogininfoId() {
        return toLogininfoId;
    }
    public void setToLogininfoId(Long toLogininfoId) {
        this.toLogininfoId = toLogininfoId;
    }
    @Override
    public String toString() {
        return "PaymentScheduleDetail [bidAmount=" + bidAmount + ", bidId=" + bidId + ", totalAmount=" + totalAmount
                + ", principal=" + principal + ", interest=" + interest + ", monthIndex=" + monthIndex + ", deadline="
                + deadline + ", bidRequestId=" + bidRequestId + ", payDate=" + payDate + ", returnType=" + returnType
                + ", paymentScheduleId=" + paymentScheduleId + ", fromLogininfo=" + fromLogininfo + ", toLogininfoId="
                + toLogininfoId + "]";
    }
}
