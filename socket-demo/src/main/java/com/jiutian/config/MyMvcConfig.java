package com.jiutian.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * ClassName:MyMvcConfig
 * Package:com.jiutian.config
 * Description:
 *
 * @Date: 2021/10/31 18:02
 * @Author: jiutian
 */

@Configuration
public class MyMvcConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // / 或 /index.html ->  index (index.html)
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/index.html").setViewName("index");
        registry.addViewController("/login.html").setViewName("login");
        registry.addViewController("/regist.html").setViewName("regist");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginHandlerInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/toLogin","/login.html", "/user/login",
                        "/toRegist","/regist.html","/user/regist",
                        "/css/**","/js/**","/img/**","/fonts/**","/images/**");
    }
}
