package com.jiutian.dao;

import com.jiutian.pojo.User;

/**
 * @Date: 2022/3/20 21:09
 * @Author: jiutian
 * @Description:
 */
public interface UserDao {
    User getUserById(Long id);
    User getUserByName(String userName);
}
