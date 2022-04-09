package com.jiutian.controller;

import cn.hutool.core.util.StrUtil;
import com.jiutian.dao.UserDao;
import com.jiutian.dao.UserDaoImpl;
import com.jiutian.pojo.User;

/**
 * @Date: 2022/3/20 21:08
 * @Author: jiutian
 * @Description:
 */
public class UserController {

    private final UserDao userDao = new UserDaoImpl();

    public String login(Long id, String pwd) {

        if(id == null || pwd == null){
            return "输入不合法";
        }
        String msg;
        User user = userDao.getUserById(id);
        System.out.println("user==>" + user);
        if (user != null) {
            if (StrUtil.equals(pwd, user.getPwd())) {
              msg = "登录成功！";
            } else {
                msg = "密码错误";
            }
        } else {
            msg = "账号不存在";
        }
        return msg;
    }
}
