package com.jizheping.api.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginInfo {
    private Long id;

    //用户状态
    public static final int STATE_NORMAL = 0;
    public static final int STATE_LOCK = 1;
    public static final int STATE_DELETE = -1;

    //前后台用户
    public static final int USERTYPE_NORMAL = 0;
    public static final int USERTYPE_SYSTEM = 1;

    //用户名
    private String username;
    //密码
    private String password;
    //用户状态
    private int state;
    //用户为前台或后台账户
    private int usertype;
    private boolean admin = false;

    //初始化账号信息
    public static LoginInfo empty(Long id){
        LoginInfo loginInfo = new LoginInfo();
        loginInfo.setId(id);

        return loginInfo;
    }
}
