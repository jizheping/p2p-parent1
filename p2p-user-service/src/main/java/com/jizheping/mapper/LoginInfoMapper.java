package com.jizheping.mapper;

import com.jizheping.api.entity.LoginInfo;
import com.jizheping.api.entity.UserInfo;

public interface LoginInfoMapper {
    LoginInfo getUserInfoByUserName(String username);

    void insertLoginInfo(LoginInfo loginInfo);

    LoginInfo login(String username);
}
