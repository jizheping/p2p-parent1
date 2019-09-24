package com.jizheping.api.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jizheping.api.util.BidConst;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 还款计划实体
 */

public class PaymentSchedule {
    private Long id;
    private Long bidRequestId; // 对应借款
    private String bidRequestTitle;//借款名称
    private LoginInfo borrowUser; // 借款人
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:SS")
    private Date deadLine; // 本期还款截止期限
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:SS")
    private Date payDate;// 还款时间
    private BigDecimal totalAmount = BidConst.ZERO; // 本期还款总金额，利息 +本金
    private BigDecimal principal = BidConst.ZERO; // 本期还款本金
    private BigDecimal interest = BidConst.ZERO; // 本期还款总利息
    private int monthIndex; // 第几期 (即第几个月)
    private int state = BidConst.PAYMENT_STATE_NORMAL; // 本期还款状态（默认正常待还）
    private int bidRequestType; // 借款类型
    private int returnType; // 还款方式，等同借款(BidRequest)中的还款方式
    // 本期还款计划对应的还款计划明细
    private List<PaymentScheduleDetail> paymentScheduleDetails = new ArrayList<PaymentScheduleDetail>();

    public PaymentSchedule(Long bidRequestId, String bidRequestTitle, LoginInfo borrowUser, Date deadLine, Date payDate,
                           BigDecimal totalAmount, BigDecimal principal, BigDecimal interest, int monthIndex, int state,
                           int bidRequestType, int returnType, List<PaymentScheduleDetail> paymentScheduleDetails) {
        super();
        this.bidRequestId = bidRequestId;
        this.bidRequestTitle = bidRequestTitle;
        this.borrowUser = borrowUser;
        this.deadLine = deadLine;
        this.payDate = payDate;
        this.totalAmount = totalAmount;
        this.principal = principal;
        this.interest = interest;
        this.monthIndex = monthIndex;
        this.state = state;
        this.bidRequestType = bidRequestType;
        this.returnType = returnType;
        this.paymentScheduleDetails = paymentScheduleDetails;
    }





    public Long getId() {
        return id;
    }



    public void setId(Long id) {
        this.id = id;
    }



    public String getStateDisplay() {
        switch (state) {
            case BidConst.PAYMENT_STATE_NORMAL:
                return "正常待还";
            case BidConst.PAYMENT_STATE_DONE:
                return "已还";
            case BidConst.PAYMENT_STATE_OVERDUE:
                return "逾期";
            default:
                return "未知";
        }
    }



    @Override
    public String toString() {
        return "PaymentSchedule [bidRequestId=" + bidRequestId + ", bidRequestTitle=" + bidRequestTitle
                + ", borrowUser=" + borrowUser + ", deadLine=" + deadLine + ", payDate=" + payDate + ", id=" + id
                + ", totalAmount=" + totalAmount + ", principal=" + principal + ", interest=" + interest
                + ", monthIndex=" + monthIndex + ", state=" + state + ", bidRequestType=" + bidRequestType
                + ", returnType=" + returnType + ", paymentScheduleDetails=" + paymentScheduleDetails + "]";
    }



    public Long getBidRequestId() {
        return bidRequestId;
    }



    public void setBidRequestId(Long bidRequestId) {
        this.bidRequestId = bidRequestId;
    }



    public String getBidRequestTitle() {
        return bidRequestTitle;
    }



    public void setBidRequestTitle(String bidRequestTitle) {
        this.bidRequestTitle = bidRequestTitle;
    }



    public LoginInfo getBorrowUser() {
        return borrowUser;
    }



    public void setBorrowUser(LoginInfo borrowUser) {
        this.borrowUser = borrowUser;
    }



    public Date getDeadLine() {
        return deadLine;
    }



    public void setDeadLine(Date deadLine) {
        this.deadLine = deadLine;
    }



    public Date getPayDate() {
        return payDate;
    }



    public void setPayDate(Date payDate) {
        this.payDate = payDate;
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



    public int getState() {
        return state;
    }



    public void setState(int state) {
        this.state = state;
    }



    public int getBidRequestType() {
        return bidRequestType;
    }



    public void setBidRequestType(int bidRequestType) {
        this.bidRequestType = bidRequestType;
    }



    public int getReturnType() {
        return returnType;
    }



    public void setReturnType(int returnType) {
        this.returnType = returnType;
    }



    public List<PaymentScheduleDetail> getPaymentScheduleDetails() {
        return paymentScheduleDetails;
    }



    public void setPaymentScheduleDetails(List<PaymentScheduleDetail> paymentScheduleDetails) {
        this.paymentScheduleDetails = paymentScheduleDetails;
    }



    public PaymentSchedule() {
        super();
    }
}
