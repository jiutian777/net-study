package com.jiutian.tcp;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * ClassName:SendImpl
 * Package:com.jiutian
 * Description:
 *
 * @Date: 2021/10/28 22:23
 * @Author: jiutian
 */
public class SendImpl implements Runnable {

    private Socket socket;

    public SendImpl(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                System.out.println("请输入要发送的数据：");
                String content = scanner.next();
                OutputStream outputStream = socket.getOutputStream();
                PrintStream printStream = new PrintStream(outputStream);
                printStream.println(content);
                printStream.close();
                printStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
