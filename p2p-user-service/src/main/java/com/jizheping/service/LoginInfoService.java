package com.jizheping.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface LoginInfoService {
    boolean registerUser(String username, String password);

    boolean login(String username, String password, HttpServletRequest request, HttpServletResponse response);
}
