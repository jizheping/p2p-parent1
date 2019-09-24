package com.jizheping.api.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class QueryObject {
    //当前页
    private int currentPage = 1;
    //分页单位
    private int pageSize = 10;

    public int getStart(){
        return (currentPage -1) * pageSize;
    }
}
