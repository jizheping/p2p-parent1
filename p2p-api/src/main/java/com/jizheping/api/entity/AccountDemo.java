package com.jizheping.api.entity;

import com.jizheping.api.util.BidConst;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.function.BiFunction;

/**
 * 用于列表展示的实体类
 * 属性取自Account类
 */

@Table(name = "account")
public class AccountDemo {
    @Id
    private Long id;

    //乐观锁版本号
    @Column(name = "version")
    private Integer version;

    //交易密码
    @Column(name = "tradePassword")
    private String tradePassword;

    //可用余额
    @Column(name = "usableAmount")
    private BigDecimal usableAmount = BidConst.ZERO;

    //冻结金额
    @Column(name = "freezedAmount")
    private BigDecimal freezedAmount = BidConst.ZERO;

    //账户待收利息
    @Column(name = "unReceiveInterest")
    private BigDecimal unReceiveInterest = BidConst.ZERO;

    //帐户待收本金
    @Column(name = "unReceivePrincipal")
    private BigDecimal unReceivePrincipal;

    //账户待还金额
    @Column(name = "unReturnAmount")
    private BigDecimal unRetuenAmount;

    //剩余授信额度
    @Column(name = "remainBorrowLimit")
    private BigDecimal remainBorrowLimit = BidConst.ZERO;

    //授信额度
    @Column(name = "borrowLimitAmount")
    private BigDecimal borrowLimitAmount;
}
