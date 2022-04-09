package com.jiutian.config;

import org.springframework.lang.NonNull;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ClassName:LoginHandlerInterceptor
 * Package:com.jiutian.config
 * Description:
 *
 * @Date: 2021/11/1 22:11
 * @Author: jiutian
 */
public class LoginHandlerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) {

        // Object loginUser = request.getSession().getAttribute(UserConstant.USER_SESSION);
        // if (loginUser == null) {
        //     request.setAttribute("msg", "没有权限，请先登录！");
        //     request.getRequestDispatcher("/login.html").forward(request, response);
        //     return false;
        // }
        return true;
    }
}
