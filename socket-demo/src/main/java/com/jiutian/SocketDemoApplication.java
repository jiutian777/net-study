package com.jiutian;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Date: 2022/3/19 22:00
 * @Author: jiutian
 * @Description:
 */
@MapperScan("com.jiutian.mapper")
@SpringBootApplication
public class SocketDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(SocketDemoApplication.class,args);
    }
}
