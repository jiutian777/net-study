package com.jiutian.config.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Date: 2022/4/3 22:39
 * @Author: jiutian
 * @Description:   授权失败处理
 */
@Component
public class DefaultAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        System.out.println("authentication success but AccessDenied");
        response.sendRedirect("/error/403");
    }
}

