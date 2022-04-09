package com.jiutian;

import com.jiutian.pojo.vo.User;
import com.jiutian.service.impl.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @Date: 2022/3/19 22:10
 * @Author: jiutian
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MyTest {

    @Autowired
    private UserServiceImpl userService;

    @Test
    public void test(){
        List<User> list = userService.list(null);
        list.forEach(System.out::println);
    }
}
