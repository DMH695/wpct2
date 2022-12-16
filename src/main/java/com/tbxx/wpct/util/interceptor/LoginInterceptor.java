package com.tbxx.wpct.util.interceptor;

import com.tbxx.wpct.util.UserHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import reactor.util.annotation.Nullable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author ZXX
 * @ClassName LoginInterceptor
 * @Description 登录校验
 * @DATE 2022/9/30 13:27
 */


public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (UserHolder.getUser() == null) {
            response.setStatus(401);
            return false;
        }
        // 有用户，则放行
        return true;
    }
}
