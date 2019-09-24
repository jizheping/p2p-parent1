package com.jizheping.api.entity;

import com.alibaba.fastjson.JSONObject;
import com.jizheping.api.util.BidConst;
import javafx.scene.media.VideoTrack;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@Table(name = "bidrequest")
public class BidRequest {
    @Id
    private Long id;

    //乐观锁版本号
    private Integer version;
    //还款方式
    private Integer returnType = BidConst.RETURN_TYPE_MONTH_INTERST_PRINCIPAL;
    //标的类型
    private Integer bidRequestType = BidConst.BIDREQUEST_TYPE_NORMAL;
    //标的状态
    private Integer bidRequestState = BidConst.BIDREQUEST_STATE_PUBLISH_PENDING;
    //借款金额
    private BigDecimal bidRequestAmount = BidConst.ZERO;
    //借款利率
    private BigDecimal currentRate = BidConst.ZERO;
    //最小投标金额
    private BigDecimal minBidAmount = BidConst.SMALLEST_BID_AMOUNT;
    //借款期限
    private int monthes2Return = 1;
    //已有投标数(已经有几个人进行投标)
    private int bidCount = 0;
    //总利息
    private BigDecimal totalRewardAmount = BidConst.ZERO;
    //已经借到的资金数量
    private BigDecimal currentSum = BidConst.ZERO;
    //标的名称
    private String title = "";
    //标的详细描述
    private String description = "";
    //招标到期时间(什么时候截止招标)
    private Date disableDate = new Date();
    //标的有效天数
    private int disableDays = 0;
    //发标人(对应createuser_id)
    private LoginInfo createUser;
    //申请时间
    private Date applyDate;
    //SimpleDateFormat格式化后的申请时间，用于前台展示
    private String applyTime;
    //发布时间(标的审核通过的时间)
    private Date publishDate;

    //获取到进度条
    public BigDecimal getPersent(){
        return this.currentSum.divide(this.bidRequestAmount, BidConst.SCALE_DISPLAY, RoundingMode.HALF_UP);

    }
    public String getBidRequestStateDisplay() {
        switch (this.bidRequestState) {
            case BidConst.BIDREQUEST_STATE_PUBLISH_PENDING:
                return "待发布";
            case BidConst.BIDREQUEST_STATE_BIDDING:
                return "招标中";
            case BidConst.BIDREQUEST_STATE_UNDO:
                return "已撤销";
            case BidConst.BIDREQUEST_STATE_BIDDING_OVERDUE:
                return "流标";
            case BidConst.BIDREQUEST_STATE_APPROVE_PENDING_1:
                return "满标一审";
            case BidConst.BIDREQUEST_STATE_APPROVE_PENDING_2:
                return "满标二审";
            case BidConst.BIDREQUEST_STATE_REJECTED:
                return "满标审核被拒";
            case BidConst.BIDREQUEST_STATE_BACK:
                return "还款中";
            case BidConst.BIDREQUEST_STATE_PAY_BACK:
                return "完成";
            case BidConst.BIDREQUEST_STATE_OVERDUE:
                return "逾期";
            case BidConst.BIDREQUEST_STATE_PUBLISH_REFUSE:
                return "发标拒绝";
            default:
                return "";
        }
    }
    public String getReturnTypeDisplay() {
        switch (returnType) {
            case BidConst.RETURN_TYPE_MONTH_INTERST_PRINCIPAL:
                return "等额本息";

            case BidConst.RETURN_TYPE_MONTH_INTERST:
                return "按月到期";

            case BidConst.RETURN_TYPE_MONTH_PRINCIPAL:
                return "等额本金";

            default:
                return "";
        }
    }

    //获取对应对象的JSON串
    public String getJsonString() {
        Map<String, Object> json = new HashMap<>();
        json.put("id", id);
        json.put("username", this.createUser.getUsername());
        json.put("title", title);
        json.put("bidRequestAmount", bidRequestAmount);
        json.put("currentRate", currentRate);
        json.put("monthes2Return", monthes2Return);
        json.put("returnType", getReturnTypeDisplay());
        json.put("totalRewardAmount", totalRewardAmount);
        return JSONObject.toJSONString(json);
    }


    //获取剩余还未投满的金额 (+:add  -:subtract * :multiply  / :divide)
    public BigDecimal  getRemainAmount(){
        return this.bidRequestAmount.subtract(this.currentSum);
    }
}
