package com.jizheping.service.Impl;

import com.jizheping.api.entity.UserInfo;
import com.jizheping.mapper.UserInfoMapper;
import com.jizheping.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Autowired
    private UserInfoMapper userInfoMapper;

    /**
     * 根据主键查询用户的详细信息
     * @param id    用户主键id
     * @return
     */
    @Override
    public UserInfo selectById(Long id) {
        UserInfo userInfo = userInfoMapper.selectUserInfoById(id);

        return userInfo;
    }
}
