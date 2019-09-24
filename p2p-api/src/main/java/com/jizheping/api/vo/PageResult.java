package com.jizheping.api.vo;

import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * 封装分页返回信息
 */

@ToString
public class PageResult {
    //总记录数
    private Integer totalCount;
    //分页单位
    private Integer pageSize = 10;
    //默认当前页
    private Integer currentPage = 1;
    //查询结果集
    private List result;

    public PageResult(Integer totalCount, Integer pageSize, Integer currentPage, List result) {
        this.totalCount = totalCount;
        this.pageSize = pageSize;
        this.currentPage = currentPage;
        this.result = result;
    }

    public PageResult(){

    }

    public static PageResult empty(int pageSize){
        return new PageResult(0,pageSize,1,new ArrayList());
    }

    /**
     * 获取总页数
     * @return
     */
    public Integer getTotalPage(){
        return Math.max((totalCount + pageSize - 1) / pageSize,1);
    }

    /**
     * 获取上一页
     * @return
     */
    public Integer gerPrev(){
        return Math.max(currentPage - 1,1);
    }

    /**
     * 获取下一页
     * @return
     */
    public Integer getNext(){
        return Math.min(currentPage + 1,getTotalPage());
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public List getResult() {
        return result;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public void setResult(List result) {
        this.result = result;
    }

}
