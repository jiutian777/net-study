package com.jiutian.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiutian.handler.api.ResultBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Date: 2022/4/3 19:51
 * @Author: jiutian
 * @Description:
 */
@Component
public class MyAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private OnSessionInvalid onSessionInvalid;

    // 认证（登录）成功
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        System.out.println("authentication---success");
        Object principal = authentication.getPrincipal();
        // 匿名用户
        if ("anonymousUser".equals(principal)) {
            System.out.println("anonymous");
        } else {
            String name = authentication.getName();
            System.out.println(name + " has login");
            if (onSessionInvalid != null) {
                // 同一用户多端登录踢下线处理
                onSessionInvalid.callLogOut(name);
            }
            response.setContentType("application/json;charset=utf-8");
            PrintWriter out = response.getWriter();
            out.write(new ObjectMapper()
                    .writeValueAsString(ResultBody.success()));
            out.flush();
            out.close();
        }
    }

    public interface OnSessionInvalid {
        void callLogOut(String name);
    }
}
