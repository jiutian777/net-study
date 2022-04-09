package com.jiutian.controller;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiutian.config.security.MyAuthenticationSuccessHandler;
import com.jiutian.handler.api.ResultBody;
import com.jiutian.pojo.Message;
import com.jiutian.pojo.vo.User;
import com.jiutian.service.impl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Date: 2022/3/20 16:48
 * @Author: jiutian
 * @Description: ServerEndpoint: 聊天室访问地址
 */
@RestController
@ServerEndpoint("/chat-room/{username}")
public class SocketController implements MyAuthenticationSuccessHandler.OnSessionInvalid {

    private static UserServiceImpl userService;

    /**
     * userService 必须是 static 的，且不能通过构造器注入
     */
    @Autowired
    public void setUserService(UserServiceImpl userService) {
        SocketController.userService = userService;
    }

    private static final Logger log = LoggerFactory.getLogger(SocketController.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 存放每个客户端对应的WebSocketServer对象。
     */
    public static Map<String, Session> sessionPools = new ConcurrentHashMap<>();

    /**
     * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
     */
    private static final AtomicInteger onlineNum = new AtomicInteger();


    /**
     * 建立 WebSocket 连接时触发
     */
    @OnOpen
    public void OnOpen(@PathParam("username") String name, Session session) throws JsonProcessingException {
        sessionPools.put(name, session);
        incrOnlineCount();
        System.out.println(name + "加入webSocket！当前人数为" + onlineNum);
        String message = "欢迎用户[ " + name + " ]来到聊天室";
        System.out.println(message);
        sendMessageAll(objectMapper.writeValueAsString
                (ResultBody.success("210",message)));
    }

    /**
     * 客户端监听服务端事件，当服务端向客户端推送消息时会被监听到
     */
    @OnMessage
    public void onMessage(@PathParam("username") String username, String message) throws JsonProcessingException {
        User user = userService.getOne(new LambdaQueryWrapper<User>()
                .eq(User::getUserName, username));

        if (user != null) {
            String sendTime = DateUtil.format(new Date(), DatePattern.NORM_DATETIME_PATTERN);
            Message msg = new Message();
            msg.setSenderName(username).setMessage(message)
                    .setSendTime(sendTime)
                    .setSenderImg(user.getUserImage());
            sendMessageAll(objectMapper
                    .writeValueAsString(ResultBody.success(msg)));
        }
    }

    /**
     * 关闭 WebSocket 连接时触发
     */
    @OnClose
    public void onClose(@PathParam("username") String username, Session session) throws JsonProcessingException {
        //移除session
        sessionPools.remove(username);
        decrOnlineCount();
        //通知他人
        String msg = "用户[ " + username + " ]已经离开聊天室";
        sendMessageAll(objectMapper
                .writeValueAsString(ResultBody.success("210",msg)));
        try {
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发生错误时触发
     */
    @OnError
    public void onError(Session session, Throwable throwable) {
        try {
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        throwable.printStackTrace();
    }

    /**
     * 接收消息请求地址
     */
    @GetMapping("/chat-room/{sender}/to/{receive}")
    public void sendToPoint(@PathVariable("sender") String sender,
                            @PathVariable("receive") String receive,
                            String message) {
        sendMessage(sessionPools.get(receive), "[" + sender + "]:" + message);
    }

    public static void incrOnlineCount() {
        onlineNum.incrementAndGet();
    }

    public static void decrOnlineCount() {
        onlineNum.decrementAndGet();
    }

    public static void sendMessageAll(String message) {
        sessionPools.forEach((sessionId, session) -> sendMessage(session, message));
    }

    public static void sendMessageTo(String receive) throws JsonProcessingException {
        String sessionInvalid = objectMapper
                .writeValueAsString(ResultBody.error("403", "您的登录已经超时或者已经在另一台机器登录，您被迫下线。"));
        sendMessage(sessionPools.get(receive), sessionInvalid);
    }

    /**
     * 发送给指定用户
     *
     * @param session 指定用户
     * @param message 消息
     */
    public static void sendMessage(Session session, String message) {
        if (session == null) {
            return;
        }
        final RemoteEndpoint.Basic basic = session.getBasicRemote();
        if (basic == null) {
            return;
        }
        try {
            basic.sendText(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void callLogOut(String name) {
        if (sessionPools.containsKey(name)) {
            try {
                System.out.println("call " + name + " LogOut");
                sendMessageTo(name);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
