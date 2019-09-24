package com.jizheping.service.impl;

import com.jizheping.api.entity.LoginInfo;
import com.jizheping.mapper.LoginMapper;
import com.jizheping.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private LoginMapper loginMapper;

    @Override
    public LoginInfo login(String username) {
        return loginMapper.login(username);
    }
}
