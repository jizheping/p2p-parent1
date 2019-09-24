package com.jizheping.api.vo;

import com.jizheping.api.util.DateUtil;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


@Data
@ToString
public class BaseAuditQueryObject extends QueryObject{

    private int state = -1;
    //时间区间查询的起始时间
    private Date beginDate;
    //时间区间查询的结束时间
    private Date endDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public void setBeginDate(Date beginDate){
        this.beginDate = beginDate;
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public void setEndDate(Date endDate){
        this.endDate = endDate;
    }

    public Date getEndDate(){
        if(endDate != null){
            return DateUtil.endOfDate(endDate);
        }

        return null;
    }
}
