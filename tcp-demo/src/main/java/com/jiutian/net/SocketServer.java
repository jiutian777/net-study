package com.jiutian.net;


import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class SocketServer {

    /**
     * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
     */
    private static final AtomicInteger onlineNum = new AtomicInteger();

    private static final ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
            .setNameFormat("demo-pool-%d").build();

    private static final ExecutorService pool = new ThreadPoolExecutor(5, 10,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

    private static final ExecutorService heartBeatPool = new ThreadPoolExecutor(5, 10,
            0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());


    public static void main(String[] args) {
        try {
            // 创建服务端socket
            ServerSocket serverSocket = new ServerSocket(10002);

            // 创建连接客户端socket
            Socket socket;

            System.out.println("服务器启动成功！等待连接......");
            //循环监听等待客户端的连接
            while (true) {
                // 监听客户端
                socket = serverSocket.accept();

                if (socket != null) {
                    // 在线人数加 1
                    incrOnlineCount();
                    InetAddress address = socket.getInetAddress();
                    System.out.println("当前客户端的IP：" + address.getHostAddress());
                    System.out.println("当前人数为:" + onlineNum);

                    pool.execute(new ServerRunnable(socket));

                    Socket finalSocket = socket;
                    heartBeatPool.execute(() -> {
                        while (true) {
                            try {
                                Thread.sleep(3000);
                                // 往输出流发送一个字节的数据，
                                // 只要对方 Socket的SO_OOBINLINE属性没有打开（默认关闭），
                                // 就会自动舍弃这个字节，抛出异常则说明断开连接
                                finalSocket.sendUrgentData(0xFF);
                            } catch (Exception e) {
                                System.out.println("客户端[" + address.getHostAddress() + "]已断开连接");
                                decrOnlineCount();
                                System.out.println("当前人数为:" + onlineNum);
                                // 断开连接则关闭定时器
                                break;
                            }finally {
                                try {
                                    finalSocket.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public static void incrOnlineCount() {
        onlineNum.incrementAndGet();
    }

    public static void decrOnlineCount() {
        onlineNum.decrementAndGet();
    }
}
