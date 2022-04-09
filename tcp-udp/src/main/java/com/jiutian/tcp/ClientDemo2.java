package com.jiutian.tcp;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.*;

/**
 * ClassName:SendDemo
 * Package:com.jiutian
 * Description:
 *
 * @Date: 2021/10/28 22:22
 * @Author: jiutian
 */
public class ClientDemo2 {
    private static final ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
            .setNameFormat("demo-pool-%d").build();

    private static final ExecutorService pool = new ThreadPoolExecutor(5, 10,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.print("请输入服务端的端口号：");
        int port = sc.nextInt();
        // 本地IP
        Socket socket = new Socket(InetAddress.getLocalHost(), port);
        // 发送的线程
        pool.execute(new SendImpl(socket));
        // 接收的线程
        pool.execute(new ReceiveImpl(socket,"张三"));
    }
}
