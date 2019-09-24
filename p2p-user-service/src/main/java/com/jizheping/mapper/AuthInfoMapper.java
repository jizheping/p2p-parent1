package com.jizheping.mapper;

import com.jizheping.api.entity.RealAuth;
import com.jizheping.api.vo.ResultVO;

public interface AuthInfoMapper {
    RealAuth getAuthInfoByapplierId(Long id);
}
