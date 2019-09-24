package com.jizheping.api.entity;

import com.jizheping.api.util.BidConst;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * 账户实体类
 */

@Entity
@Table(name = "account")
@Data
public class Account {
    @Id
    private Long id;
    //交易密码
    @Column(name = "tradePassword")
    private String tradePassword;
    //可用余额
    @Column(name = "usableAmount")
    private BigDecimal usableAmount = BidConst.ZERO;
    //冻结金额
    @Column(name = "freezedAmount")
    private BigDecimal freezedAmount = BidConst.ZERO;
    //授信额度
    @Column(name = "borrowLimitAmount")
    private BigDecimal borrowLimitAmount;
    //乐观锁版本号
    @Column(name = "version")
    private Integer version;
    //账户待收利息
    @Column(name = "unReceiveInterest")
    private BigDecimal unReceiveInterest = BidConst.ZERO;
    //账户待收本金
    @Column(name = "unReceivePrincipal")
    private BigDecimal unReceivePrincipal = BidConst.ZERO;
    //账户待还金额
    @Column(name = "unReturnAmount")
    private BigDecimal unReturnAmount = BidConst.ZERO;
    //剩余授信额度
    @Column(name = "remainBorrowLimit")
    private BigDecimal remainBorrowLimit = BidConst.ZERO;

    //计算账户总额
    public BigDecimal getTotalAmount(){
        //可用余额 + 冻结金额 + 账户待收本金 + 账户代收利息
        return usableAmount.add(freezedAmount).add(unReceivePrincipal).add(unReceiveInterest);
    }

    //初始化一个新账号
    public static Account empty(Long id){
        Account account = new Account();

        account.setId(id);

        //对授信额度进行初始化   2000
        account.setBorrowLimitAmount(BidConst.DEFALUT_BORROW_LIMIT_AMOUNT);
        //对剩余授信额度进行初始化   2000
        account.setRemainBorrowLimit(BidConst.DEFALUT_BORROW_LIMIT_AMOUNT);
        //对乐观锁进行初始化
        account.setVersion(0);

        return account;
    }
}
