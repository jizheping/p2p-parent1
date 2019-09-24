package com.jizheping.api.entity;

import lombok.Data;

import javax.persistence.Column;

/**
 * 信用认证字典实体类
 */

@Data
public class SystemDictionaryItem {
    private Long id;
    //所属分组
    @Column(name = "parentId")
    private Long parentId;
    //中文名称
    private String title;
    //英文名称
    private String tvalue;
    //顺序号
    private int sequence;
    //备注
    private String intro;
}
