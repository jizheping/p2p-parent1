package com.jizheping.service.Impl;

import com.jizheping.api.entity.RealAuth;
import com.jizheping.api.vo.ResultVO;
import com.jizheping.mapper.AuthInfoMapper;
import com.jizheping.service.AuthInfoService;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthInfoServiceImpl implements AuthInfoService {
    @Autowired
    private AuthInfoMapper authInfoMapper;

    @Override
    public RealAuth getAuthInfoById(Long id) {
        RealAuth realAuth = authInfoMapper.getAuthInfoByapplierId(id);

        return realAuth;
    }
}
