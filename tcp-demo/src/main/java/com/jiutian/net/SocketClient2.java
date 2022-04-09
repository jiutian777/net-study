package com.jiutian.net;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class SocketClient2 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        try {
            // 和服务器创建连接
            Socket socket = new Socket("localhost", 10002);

            // 要发送给服务器的信息
            OutputStream os = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(os);
            System.out.print("用户Id：");
            String id = sc.nextLine();
            System.out.print("密码：");
            String password = sc.nextLine();
            // pw.write(id);
            // pw.write(password);
            pw.println(id);
            pw.println(password);

            pw.flush();

            socket.shutdownOutput();

            // 从服务器接收的信息
            InputStream is = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String info = null;
            while ((info = br.readLine()) != null) {
                System.out.println("我是客户端2，服务器返回信息：" + info);
            }

            br.close();
            is.close();
            os.close();
            pw.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
