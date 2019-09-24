package com.jizheping.controller;

import com.jizheping.api.entity.LoginInfo;
import com.jizheping.api.util.BidConst;
import com.jizheping.api.util.CookieUtil;
import com.jizheping.api.util.RedisUtil;
import com.jizheping.api.vo.CodeMsg;
import com.jizheping.api.vo.ResultVO;
import com.jizheping.service.LoginInfoService;
import com.netflix.discovery.converters.Auto;
import org.aspectj.apache.bcel.classfile.Code;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private LoginInfoService loginInfoService;

    //注入redis工具类(封装简单的redis操作)
    @Autowired
    private RedisUtil redisUtil;

    //用户注册
    @RequestMapping(value = "/registry",method = RequestMethod.POST)
    public ResultVO<Boolean> registrtUser(String username, String password){
        //判断所注册的用户名是否唯一,唯一进行注册操作,不唯一则返回错误信息
        boolean result = loginInfoService.registerUser(username,password);
        if(result){
            return ResultVO.success(CodeMsg.SUCECC);
        }else{
            return ResultVO.error(CodeMsg.USER_REGISTRY_FALL);
        }
    }

    //用户登录
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public ResultVO<Boolean> login(String username, String password, HttpServletRequest request, HttpServletResponse response){
        try {
            boolean result = loginInfoService.login(username,password,request,response);
        }catch (Exception e){
            e.printStackTrace();
        }

        return ResultVO.success(CodeMsg.SUCECC);
    }

    //判断redis中是否有用用户登录的缓存
    @RequestMapping(value = "/checkLogin",method = RequestMethod.POST)
    public ResultVO<Boolean> checkLogin(@CookieValue(name = BidConst.COOKIE_NAME,required = false)String token
        ,HttpServletRequest request
        ,HttpServletResponse response){
        //判断前台浏览器中是否有cookie
        if(token == null || "".equals(token)){
            return ResultVO.error(CodeMsg.USER_SESSION_EXPIRE);
        }

        //从redis中获取loginInfo用户的缓存
        LoginInfo loginInfo = redisUtil.get(token,LoginInfo.class);

        if(loginInfo != null){
            //延长cookie的有效期
                redisUtil.setex(token,loginInfo,BidConst.SESSION_EXPIRE);

                Cookie cookie = CookieUtil.getByCookieName(BidConst.COOKIE_NAME,request);
                Cookie newCookie = new Cookie(cookie.getName(),cookie.getValue());
                newCookie.setDomain("p2p.com");
                newCookie.setPath("/");

                //立即删除原来的cookie
                //先判断cookie是否存在在确定是否进行删除
                if(cookie != null){
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }

                newCookie.setMaxAge(BidConst.SESSION_EXPIRE);
                response.addCookie(newCookie);

            return ResultVO.success(loginInfo);
        }else{
            return ResultVO.success(CodeMsg.USER_SESSION_EXPIRE);
        }
    }
}
