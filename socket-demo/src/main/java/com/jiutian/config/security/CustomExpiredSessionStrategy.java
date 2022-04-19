package com.jiutian.config.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import java.io.IOException;

/**
 * @Date: 2022/4/4 12:57
 * @Author: jiutian
 * @Description:
 */
public class CustomExpiredSessionStrategy implements SessionInformationExpiredStrategy {

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    // private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException {
        User principal = (User) event.getSessionInformation().getPrincipal();
        System.out.println("CustomExpiredSessionStrategy----onExpiredSessionDetected： the user[ " +
                principal.getUsername() + " ] is sendRedirect to login page");
        redirectStrategy.sendRedirect(event.getRequest(), event.getResponse(), "/toLogin");

        // 告诉前端并发登陆异常
        // event.getResponse().setContentType("application/json;charset=UTF-8");
        // event.getResponse().getWriter().write(objectMapper
        //         .writeValueAsString(ResultBody.error("403",
        //                 "您的登录已经超时或者已经在另一台机器登录，您被迫下线。"+
        //                 event.getSessionInformation().getLastRequest())));
    }
}

