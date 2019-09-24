package com.jizheping.api.util;

import java.math.BigDecimal;

/**
 * 项目所用常量接口
 */

public interface BidConst {
    //用户会话过期时间
    public static final int SESSION_EXPIRE = 30*60;
    //token存储cookie的名称
    public static final String COOKIE_NAME = "USER_TOKEN";

    //金额常量
    public static final BigDecimal ZERO = BigDecimal.ZERO;
    //授信额度
    public static final BigDecimal DEFALUT_BORROW_LIMIT_AMOUNT = BigDecimal.valueOf(2000);
    //系统默认最小投资金额
    public static final BigDecimal SMALLEST_BID_AMOUNT = BigDecimal.valueOf(100);

    /**
     * 还款方式
     * 等额本息
     * 等额本金
     * 按月到期
     */
    public static final int RETURN_TYPE_MONTH_INTERST_PRINCIPAL = 0;
    public static final int RETURN_TYPE_MONTH_PRINCIPAL = 1;
    public static final int RETURN_TYPE_MONTH_INTERST = 2;

    /**
     * 标的类型
     * 信用贷
     * 车易贷
     * 房易贷
     */
    public static final int BIDREQUEST_TYPE_NORMAL = 0;
    public static final int BIDREQUEST_TYPE_CAR = 1;
    public static final int BIDREQUEST_TYPE_HOUSE = 2;

    /**
     * 资金精度
     */
    public static final int SCALE_CACULATE = 8;//计算精度
    public static final int SCALE_STORE = 4;//数据库存储精度
    public static final int SCALE_DISPLAY = 2;//显示精度

    //借款表状态
    //待发布(正在进行发标前审核)
    public static final int BIDREQUEST_STATE_PUBLISH_PENDING = 0;
    //招标中(等待他人投标)
    public static final int BIDREQUEST_STATE_BIDDING = 1;
    //标的已撤销(申请人主动撤销已经发布的标的,业务逻辑上:满标二审未完成前均可进行标的撤销操作)
    public static final int BIDREQUEST_STATE_UNDO = 2;
    //流标(规定时间内募集资金未达到发表人要求,发标时间一过自动变为流标状态)
    public static final int BIDREQUEST_STATE_BIDDING_OVERDUE = 3;
    //满标一审(满标后自动变为等待满标一审状态)
    public static final int BIDREQUEST_STATE_APPROVE_PENDING_1 = 4;
    //满标二审(满标一审通过后自动变为等待满标二审状态)
    public static final int BIDREQUEST_STATE_APPROVE_PENDING_2 = 5;
    //满表一审或满标二审不通过
    public static final int BIDREQUEST_STATE_REJECTED = 6;
    //满标二审通过,已经放款,正在还款状态
    public static final int BIDREQUEST_STATE_BACK = 7;
    //已还清(标的正常完成)
    public static final int BIDREQUEST_STATE_PAY_BACK = 8;
    //逾期
    public static final int BIDREQUEST_STATE_OVERDUE = 9;
    //初审拒绝(发标前审核不通过)
    public static final int BIDREQUEST_STATE_PUBLISH_REFUSE = 10;

    //后台审核状态
    public static final int STATE_APPLY = 0;//申请状态
    public static final int STATE_PASS = 1;//审核通过
    public static final int STATE_REJECT = 2;//审核失败

    //审核类型
    public static final int PUBLISH_AUDIT = 0;//发标前审核
    public static final int FULL_AUDIT1 = 1;//满标一审
    public static final int FULL_AUDIT2 = 2;//满标二审

    public final static int PAYMENT_STATE_NORMAL = 0; // 正常待还
    public final static int PAYMENT_STATE_DONE = 1; // 已还
    public final static int PAYMENT_STATE_OVERDUE = 2; // 逾期

    // 资金流水类别：充值---> 可用余额增加
    public final static int ACCOUNT_ACTIONTYPE_RECHARGE_OFFLINE = 0;

    // 资金流水类别：提现成功---> 冻结余额减少
    public final static int ACCOUNT_ACTIONTYPE_WITHDRAW = 1;
    // 资金流水类别：提现申请冻结金额--->冻结金额增加  可用余额减少
    public final static int ACCOUNT_ACTIONTYPE_WITHDRAW_FREEZED = 12;
    // 资金流水类别:提现申请失败取消冻结金额--->冻结金额减少 可用余额增加
    public final static int ACCOUNT_ACTIONTYPE_WITHDRAW_UNFREEZED = 13;

    // 资金流水类别：初步投标成功--->冻结金额增加  可用余额减少
    public final static int ACCOUNT_ACTIONTYPE_BID_FREEZED = 10;
    // 资金流水类别：最终投标成功--->冻结金额减少，待收本金、待收利息增加
    public final static int ACCOUNT_ACTIONTYPE_BID_SUCCESSFUL = 3;
    // 资金流水类别：取消投标冻结金额 --->冻结金额减少 可用余额增加
    public final static int ACCOUNT_ACTIONTYPE_BID_UNFREEZED = 11;


    // 资金流水类别：成功借款 -->可用余额增加,待还本息增加，  剩余征信额度减少
    public final static int ACCOUNT_ACTIONTYPE_BIDREQUEST_SUCCESSFUL = 2;
    // 资金流水类别：还款-->可用余额减少，剩余征信额度增加
    public final static int ACCOUNT_ACTIONTYPE_RETURN_MONEY = 4;

    // 资金流水类别：回款-->可用余额增加， 待收本金和待收利息减少
    public final static int ACCOUNT_ACTIONTYPE_CALLBACK_MONEY = 5;

    // 资金流水类别：支付平台管理费-->可用余额减少
    public final static int ACCOUNT_ACTIONTYPE_CHARGE = 6;
    // 资金流水类别：利息管理费--->可用余额减少  [回款]
    public final static int ACCOUNT_ACTIONTYPE_INTEREST_SHARE = 7;
    // 资金流水类别：提现手续费 --->可用余额减少
    public final static int ACCOUNT_ACTIONTYPE_WITHDRAW_MANAGE_CHARGE = 8;
    // 资金流水类别：充值手续费--->可用余额减少
    public final static int ACCOUNT_ACTIONTYPE_RECHARGE_CHARGE = 9;
}
