package com.jiutian.tcp;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.*;

/**
 * ClassName:ServerDemo
 * Package:com.jiutian
 * Description:
 *
 * @Date: 2021/10/28 22:46
 * @Author: jiutian
 */
public class ServerDemo {

    private static final ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
            .setNameFormat("demo-pool-%d").build();

    private static final ExecutorService pool = new ThreadPoolExecutor(5, 10,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.print("请输入服务端的端口号：");
        int port = sc.nextInt();
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("服务端已开启,等待客户端连接....");
        Socket socket = serverSocket.accept();
        //发送的线程
        pool.execute(new SendImpl(socket));
        //接收的线程
        pool.execute(new ReceiveImpl(socket,"李四"));
    }
}
