package com.jiutian.oo;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * ClassName:MyServer
 * Package:com.jiutian.tcp
 * Description:
 *
 * @Date: 2021/10/28 17:32
 * @Author: jiutian
 */
public class MyServer {

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(6666);
        System.out.println("服务端开启");

        while (true) {
            Socket socket = server.accept();
            System.out.println("响应请求！");
            new Thread(() -> {
                try {
                    InputStream is = socket.getInputStream();
                    OutputStream os = socket.getOutputStream();

                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    PrintStream ps = new PrintStream(os);

                    String line = br.readLine();
                    System.out.println("服务器接收到的数据为：" + line);

                    ps.println("我很好！");

                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}

