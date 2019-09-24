package com.jizheping.api.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class RealAuth extends BaseAuditDomain{
    public static final int SEX_MALE = 0;//男
    public static final int SEX_FEMALE = 1;//女

    private String realname;//真实姓名
    private int sex = SEX_MALE;//性别
    private String idNumber;//证件号码;
    private String birthDate;//出生日期;
    private String address;//证件地址

    private String image1;//身份证正面照片
    private String image2;//身份证背面照片

    public String getSexDisplay() {
        return sex == SEX_MALE ? "男" : "女";
    }

    public String getBirthDate(){
        String birth = this.birthDate;

        String newBirth = birth.substring(0,4) + "-" + birth.substring(4,6) + "-" +birth.substring(6);

        return newBirth;
    }

    public String getJsonString() {
        Map<String, Object> m = new HashMap<>();
        m.put("id", getId());
        m.put("username", this.getApplier().getUsername());
        m.put("realname", realname);
        m.put("idNumber", idNumber);
        m.put("sex", getSexDisplay());
        m.put("birthDate", birthDate);
        m.put("address", address);
        m.put("image1", image1);
        m.put("image2", image2);
        return JSONObject.toJSONString(m);
    }
}
