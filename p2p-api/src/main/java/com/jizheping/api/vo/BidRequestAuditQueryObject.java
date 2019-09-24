package com.jizheping.api.vo;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class BidRequestAuditQueryObject extends BaseAuditQueryObject{
    private int[] bidRequestStates;  //需要进行查询的多个值
    private String orderBy;           //按照那个列表排序
    private String orderType;         //按照什么类型排序
    private Long creatorId;           //借款人id
}
