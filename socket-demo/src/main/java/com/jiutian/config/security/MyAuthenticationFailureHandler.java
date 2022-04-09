package com.jiutian.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiutian.handler.api.ResultBody;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Date: 2022/4/3 19:52
 * @Author: jiutian
 * @Description:
 */
@Component
public class MyAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception)
            throws IOException {
        System.out.println("authentication---failure");
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.write(new ObjectMapper()
                .writeValueAsString(ResultBody.error("账号或密码错误")));
        out.flush();
        out.close();
    }
}
