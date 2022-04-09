package com.jiutian.pojo.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @Date: 2022/3/1 9:13
 * @Author: jiutian
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("chat_user")
public class UserDO implements Serializable {
    private Long id;
    private String userName;
    private String pwd;
    private String userImage;
    private String role;
    private Date createTime;
    private Date updateTime;
}
