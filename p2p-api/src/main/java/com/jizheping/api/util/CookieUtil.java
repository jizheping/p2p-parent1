package com.jizheping.api.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Cookie工具类
 */

public class CookieUtil {
    public static Cookie getByCookieName(String cookieName, HttpServletRequest request){
        Cookie[] cookies = request.getCookies();

        //判断Cookie是否存在,存在返回对应的Cookie信息，失败返回空
        if(cookies != null && cookies.length > 0){
            for(Cookie cookie : cookies){
                if(cookie.getName().equals(cookieName)){
                    return cookie;
                }
            }
        }

        return null;
    }
}
