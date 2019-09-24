package com.jizheping.api.entity;

import com.jizheping.api.util.BidConst;
import lombok.Data;

import java.util.Date;

@Data
public class BaseAuditDomain{
    private Long id;
    //申请人
    private LoginInfo applier;
    //申请时间
    private Date applyTime;
    //审核人
    private LoginInfo auditor;
    //审核时间
    private Date auditTime;
    //状态
    private int state;
    //审核备注
    private String remark;

    //可以在前台调用此方法进行选择，输出对应的审核申请状态
    public String getStateDisplay(){
        switch (state){
            case BidConst.STATE_APPLY:
                return "申请状态";
            case BidConst.STATE_PASS:
                return "审核通过";
            case BidConst.STATE_REJECT:
                return "审核失败";
            default:
                return "异常";
        }
    }
}
