package com.jizheping.api.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Value;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Table(name = "accountflow")
@Entity
public class AccountFlow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "account_id")
    private Long accountId ;  //对应的账户id
    @Column(name = "amount")
    private BigDecimal amount ; //金额
    @JsonFormat(pattern="yyyy-MM-dd")
    @Column(name = "actionTime")
    private Date actionTime ; // 业务时间
    @Column(name = "accountActionType")
    private int accountActionType ; //对应的 资金变化的类型
    @Column(name = "note")
    private String note ;
    @Column(name = "balance")
    private BigDecimal useableAmount ;  //流水之后账户的 可用金额
    @Column(name = "freezed")
    private BigDecimal freezedAmount ; //流水之后账户的  冻结金额


    @Override
    public String toString() {
        return "AccountFlow{" +
                "id=" + id +
                ", accountId=" + accountId +
                ", amount=" + amount +
                ", actionTime=" + actionTime +
                ", accountActionType=" + accountActionType +
                ", note='" + note + '\'' +
                ", useableAmount=" + useableAmount +
                ", freezedAmount=" + freezedAmount +
                '}';
    }

    public Long getId() {

        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Date getActionTime() {
        return actionTime;
    }

    public void setActionTime(Date actionTime) {
        this.actionTime = actionTime;
    }

    public int getAccountActionType() {
        return accountActionType;
    }

    public void setAccountActionType(int accountActionType) {
        this.accountActionType = accountActionType;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public BigDecimal getUseableAmount() {
        return useableAmount;
    }

    public void setUseableAmount(BigDecimal useableAmount) {
        this.useableAmount = useableAmount;
    }

    public BigDecimal getFreezedAmount() {
        return freezedAmount;
    }

    public void setFreezedAmount(BigDecimal freezedAmount) {
        this.freezedAmount = freezedAmount;
    }
}
