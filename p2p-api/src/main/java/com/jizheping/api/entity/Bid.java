package com.jizheping.api.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jizheping.api.util.BidConst;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Bid {
    private Long id;
    private BigDecimal actualRate= BidConst.ZERO;
    private BigDecimal availableAmount=BidConst.ZERO;

    private Long bidRequestId;
    private String bidRequestTitle;
    private LoginInfo bidUser;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date bidTime;

    private int bidRequestState;

    public BigDecimal getActualRate() {
        return actualRate;
    }

    public void setActualRate(BigDecimal actualRate) {
        this.actualRate = actualRate;
    }

    public BigDecimal getAvailableAmount() {
        return availableAmount;
    }

    public void setAvailableAmount(BigDecimal availableAmount) {
        this.availableAmount = availableAmount;
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

    public LoginInfo getBidUser() {
        return bidUser;
    }

    public void setBidUser(LoginInfo bidUser) {
        this.bidUser = bidUser;
    }

    public Date getBidTime() {
        return bidTime;
    }

    public void setBidTime(Date bidTime) {
        this.bidTime = bidTime;
    }

    public int getBidRequestState() {
        return bidRequestState;
    }

    public void setBidRequestState(int bidRequestState) {
        this.bidRequestState = bidRequestState;
    }
}
