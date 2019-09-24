package com.jizheping.mapper;

import com.jizheping.api.entity.LoginInfo;

public interface LoginMapper {
    LoginInfo login(String username);
}
