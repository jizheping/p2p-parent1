package com.jizheping.service.Impl;

import com.jizheping.api.entity.LoginInfo;
import com.jizheping.api.entity.UserInfo;
import com.jizheping.api.exception.GlobalException;
import com.jizheping.api.util.BidConst;
import com.jizheping.api.util.CookieUtil;
import com.jizheping.api.util.Md5Util;
import com.jizheping.api.util.RedisUtil;
import com.jizheping.api.vo.CodeMsg;
import com.jizheping.feign.AccountFeign;
import com.jizheping.mapper.LoginInfoMapper;
import com.jizheping.mapper.UserInfoMapper;
import com.jizheping.service.LoginInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class LoginInfoServiceImpl implements LoginInfoService {
    @Autowired
    private LoginInfoMapper loginInfoMapper;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private AccountFeign accountFeign;

    @Autowired
    private RedisUtil redisUtil;

    @Transactional
    @Override
    public boolean registerUser(String username, String password) {
        LoginInfo login = loginInfoMapper.getUserInfoByUserName(username);

        //校验用户名唯一
        if(login == null){
            //向logininfo(账号表)中插入数据,返回账号的主键id
            LoginInfo loginInfo = new LoginInfo();
            loginInfo.setUsername(username);
            loginInfo.setPassword(Md5Util.md5(password));

            loginInfoMapper.insertLoginInfo(loginInfo);

            Long id = loginInfo.getId();

            //使用账号表中添加返回的id向userinfo(用户信息表)插入记录
            UserInfo userInfo = UserInfo.empty(id);

            int row = userInfoMapper.insertUserInfo(userInfo);

            //使用Feign调用向account(账户表)中插入一条新记录
            accountFeign.createAccount(id);

            return row > 0;
        }

        return false;
    }

    @Override
    public boolean login(String username, String password, HttpServletRequest request, HttpServletResponse response) {
        //根据username查询用户是否存在
        LoginInfo loginInfo = loginInfoMapper.login(username);

        if(loginInfo == null){
            //用户不存在抛出用户不存在异常
            throw new GlobalException(CodeMsg.USER_NOT_EXISTS);
        }else if(!Md5Util.md5(password).equals(loginInfo.getPassword())){
            //密码错误抛出密码错误异常
            throw new GlobalException(CodeMsg.PWD_ERROR);
        }else{
            //用redis实现分布式session
            //先删除已经存在的cookie
            Cookie cookie = CookieUtil.getByCookieName(BidConst.COOKIE_NAME,request);

            if(cookie != null){
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }

            //再添加新的cookie
            //使用UUID生成随机字符串，作为redis中的token(key)
            String token = UUID.randomUUID().toString().replace("-","");

            //在浏览器中使用cookie存储

            Cookie newCookie = new Cookie("USER_TOKEN",token);

            //设置cookie的过期时间
            newCookie.setMaxAge(BidConst.SESSION_EXPIRE);
            //设置cookie的域
            newCookie.setDomain("p2p.com");
            //在同一应用服务器内共享方法
            newCookie.setPath("/");
            //将cookie返回到浏览器中
            response.addCookie(newCookie);
            //不在redis中插入账户密码(安全)
            loginInfo.setPassword(null);
            redisUtil.setex(token,loginInfo,BidConst.SESSION_EXPIRE);
        }

        return true;
    }


}
