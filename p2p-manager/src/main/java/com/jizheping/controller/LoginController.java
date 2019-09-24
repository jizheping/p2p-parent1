package com.jizheping.controller;

import com.jizheping.api.entity.LoginInfo;
import com.jizheping.api.util.Md5Util;
import com.jizheping.api.vo.CodeMsg;
import com.jizheping.api.vo.ResultVO;
import com.jizheping.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/manager")
public class LoginController {
    @Autowired
    private LoginService loginService;

    @GetMapping("/index")
    public String toIndex(Model model){
        LoginInfo loginInfo = LoginInfo.empty(5L);
        loginInfo.setUsername("admin");

        System.out.println(loginInfo);

        model.addAttribute("logininfo",loginInfo);

        return "main";
    }

    @ResponseBody
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public ResultVO login(String username,String password){
        LoginInfo loginInfo = loginService.login(username);

        if(loginInfo == null){
            return ResultVO.error(CodeMsg.ACCOUNT_NOT_EXISTS);
        }else {
            if(loginInfo.getPassword().equals(Md5Util.md5(password))){
                return ResultVO.success(CodeMsg.SUCECC);
            }else{
                return ResultVO.error(CodeMsg.PWD_ERROR);
            }
        }
    }
}
