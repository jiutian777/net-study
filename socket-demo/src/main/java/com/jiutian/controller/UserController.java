package com.jiutian.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiutian.constant.UserConstant;
import com.jiutian.handler.api.ResultBody;
import com.jiutian.pojo.vo.User;
import com.jiutian.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;

/**
 * @Date: 2022/3/1 9:23
 * @Author: jiutian
 * @Description:
 */
@Controller
@RequestMapping("/user")
public class UserController {

    private final UserServiceImpl userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/regist")
    @ResponseBody
    public ResultBody<String> regist(@RequestBody User user, HttpServletRequest request) {
        if (user == null) {
            return ResultBody.error("注册失败");
        }
        User one = userService.getOne(new LambdaQueryWrapper<User>()
                .eq(User::getUserName, user.getUserName()));
        if (one != null) {
            return ResultBody.error("该名字已被注册！");
        }
        user.setUserImage(UserConstant.USER_DEFAULT_IMG);
        user.setRole(UserConstant.USER_ROLE_USER);
        boolean save = userService.save(user);
        if (save) {
            //进行授权登录
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    user.getUserName(), user.getPwd());
            token.setDetails(new WebAuthenticationDetails(request));
            Authentication authenticatedUser = authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
            request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
            return ResultBody.success();
        }
        return ResultBody.error("注册失败");
    }

    // @PostMapping("/login")
    // public String login(@RequestParam("username") String userName,
    //                     @RequestParam("password") String pwd,
    //                     HttpSession session, Model model) {
    //     System.out.println("login");
    //     String msg;
    //     User user = userService.getOne(new LambdaQueryWrapper<User>()
    //             .eq(User::getUserName, userName));
    //     if (user != null) {
    //         if (StrUtil.equals(pwd, user.getPwd())) {
    //             session.setAttribute(UserConstant.USER_SESSION, user);
    //             return "redirect:/index.html";
    //         } else {
    //             msg = "密码错误";
    //         }
    //     } else {
    //         msg = "账号不存在";
    //     }
    //     model.addAttribute("msg", msg);
    //     return "login";
    // }

    @GetMapping("/logout")
    @ResponseBody
    public ResultBody<String> logout(HttpSession session) {
        // 清空 session
        session.invalidate();
        return ResultBody.success();
    }

    @PostMapping("/getByUserName")
    @ResponseBody
    public ResultBody<String> getByUserName(Principal principal) throws JsonProcessingException {
        if (principal != null) {
            String userName = principal.getName();
            User user = userService.getOne(
                    new LambdaQueryWrapper<User>().eq(User::getUserName, userName));
            if (user != null) {
                return ResultBody.success(new ObjectMapper().writeValueAsString(user));
            }
        }
        return ResultBody.error("用户名或密码错误");
    }


    @PostMapping("/update")
    @ResponseBody
    public ResultBody<String> updateUser(@RequestBody User user) {
        String userName = user.getUserName();
        boolean b = userService.update(user, new LambdaUpdateWrapper<User>()
                .eq(User::getUserName, userName));
        if (b) {
            return ResultBody.success();
        }
        return ResultBody.error("error");
    }
}
