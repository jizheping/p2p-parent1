package com.jizheping.service;

import com.jizheping.api.entity.UserInfo;

public interface UserInfoService {
    /**
     * 根据id查询用户的详细信息
     * @param id    用户主键id
     * @return
     */
    UserInfo selectById(Long id);
}
