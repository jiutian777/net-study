package com.jiutian.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Date: 2022/3/20 10:57
 * @Author: jiutian
 * @Description:
 */
@Controller
public class RouterController {

    @GetMapping({"/","/index"})
    public String index(){
        return "index";
    }

    @GetMapping("/toLogin")
    public String toLogin(){
        return "login";
    }

    @GetMapping("/toRegist")
    public String toRegist(){
        return "regist";
    }

    @GetMapping("/error/403")
    public String to403(){
        return "error/403";
    }

    @GetMapping("/error/404")
    public String to404(){
        return "error/404";
    }


}
