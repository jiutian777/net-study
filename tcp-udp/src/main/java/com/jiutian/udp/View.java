package com.jiutian.udp;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.net.DatagramSocket;
import java.util.Scanner;
import java.util.concurrent.*;

/**
 * @author jiutian
 */
public class View {

    private static final ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
            .setNameFormat("demo-pool-%d").build();

    private static final ExecutorService pool = new ThreadPoolExecutor(5, 10,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入当前程序的端口号：");
        int myPort = sc.nextInt();
        System.out.println("请输入对方的姓名:");
        String userName = sc.next();
        System.out.println("请输入对方的ip地址:");
        String address = sc.next();
        System.out.println("请输入对方的端口号：");
        int sendPort = sc.nextInt();
        System.out.println("聊天系统初始化完成并启动！");
        DatagramSocket socket = new DatagramSocket(myPort);
        pool.execute(new ReceiveSocket(socket, userName));
        pool.execute(new SendSocket(socket, sendPort, address));
    }
}
