package com.jizheping.controller;

import com.jizheping.api.entity.RealAuth;
import com.jizheping.api.entity.UserInfo;
import com.jizheping.api.vo.ResultVO;
import com.jizheping.service.AuthInfoService;
import com.jizheping.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/personal")
public class PersonalController {
    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private AuthInfoService authInfoService;

    //获取用户详细信息
    @RequestMapping("/getUserMessage")
    public ResultVO<UserInfo> login(Long userId){
        UserInfo userInfo = userInfoService.selectById(userId);

        return ResultVO.success(userInfo);
    }

    @RequestMapping("/getAuthInfoById")
    public ResultVO<RealAuth> getAuthInfoById(Long id){
        try {
            RealAuth realAuth = authInfoService.getAuthInfoById(id);

            return ResultVO.success(realAuth);
        }catch (Exception e){
            e.printStackTrace();
            return ResultVO.success(null);
        }
    }
}
