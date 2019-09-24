package com.jizheping.api.entity;

import com.jizheping.api.util.BitStatesUtils;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

/**
 * 用户信息实体类
 */

@Getter
@Setter
@Data
public class UserInfo {
    private Long id;
    //乐观锁版本号
    private int version;
    //位状态信息
    @Column(name = "bitState")
    private Long bitState;
    //真实姓名
    @Column(name = "realName")
    private String realName;
    //身份证号
    @Column(name = "idNumber")
    private String idNumber;
    //手机号码
    @Column(name = "phoneNumber")
    private String phoneNumber;
    //收入水平
    @Column(name = "incomeGrade_id")
    private SystemDictionaryItem incomeGrade;
    //婚姻状况
    private SystemDictionaryItem marriage;
    //子女状况
    @Column(name = "kidCount_id")
    private SystemDictionaryItem kidCount;
    //教育背景
    @Column(name = "educationBackground_id")
    private SystemDictionaryItem educationBackground;
    //住房条件
    @Column(name = "houseCondition_id")
    private SystemDictionaryItem houseCondition;
    //邮箱地址
    private String email;
    //授信评分
    @Column(name = "authScore")
    private int authScore;
    //实名认证id
    @Column(name = "realauthId")
    private Long realauthId;

    //调用位运算工具类进行位状态的添加
    public void addState(Long state){
        this.bitState = BitStatesUtils.addState(this.bitState,state);
    }

    //调用位运算工具类行位状态的移除
    public void removeState(Long state){
        this.bitState = BitStatesUtils.removeState(this.bitState,state);
    }

    //判断当前位状态中是否进行手机号码认证
    public boolean getIsBindPhone(){
        return BitStatesUtils.hasState(this.bitState,BitStatesUtils.OP_BIND_PHONE);
    }

    //判断当前位状态中是否进行邮箱认证
    public boolean getIsBindEmail(){
        return BitStatesUtils.hasState(this.bitState,BitStatesUtils.OP_BIND_EMAIL);
    }

    //判断当前位状态是否进行基本信息填写
    public boolean getBaseInfo(){
        return BitStatesUtils.hasState(this.bitState,BitStatesUtils.OP_BASE_INFO);
    }

    //判断当前位状态是否进行实名认证
    public boolean getRealAuth(){
        return BitStatesUtils.hasState(this.bitState,BitStatesUtils.OP_REAL_AUTH);
    }

    //判断当前位状态是否进行视频认证
    public boolean getVedioAuth(){
        return BitStatesUtils.hasState(this.getBitState(),BitStatesUtils.OP_VEDIO_AUTH);
    }

    //判断当前位状态是否拥有贷款
    public boolean getHasBidRequest(){
        return BitStatesUtils.hasState(this.getBitState(),BitStatesUtils.OP_HAS_BIDREQUEST);
    }

    /*//获取对应对象的实名认证的姓名
    public String getAnonymousRealName(){
        String realName = this.realName;
        int len = realName.length();
        String name = realName.substring(0,1);
        for(int i=1;i<len;i++){
            name += "*";
        }
        return name;
    }*/

    //创建一个userinfo对象
    public static UserInfo empty(Long id){
        UserInfo userInfo = new UserInfo();

        userInfo.setId(id);
        userInfo.setBitState(BitStatesUtils.OP_BASIC_INFO);

        return userInfo;
    }
}
