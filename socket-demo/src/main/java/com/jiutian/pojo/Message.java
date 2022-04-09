package com.jiutian.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Date: 2022/3/20 17:40
 * @Author: jiutian
 * @Description:
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class Message implements Serializable {
    private String senderName;
    private String senderImg;
    private String message;
    private String sendTime;
}
