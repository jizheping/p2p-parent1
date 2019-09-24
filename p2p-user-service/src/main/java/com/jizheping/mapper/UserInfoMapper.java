package com.jizheping.mapper;

import com.jizheping.api.entity.UserInfo;

public interface UserInfoMapper {
    int insertUserInfo(UserInfo userInfo);

    UserInfo selectUserInfoById(Long id);
}
