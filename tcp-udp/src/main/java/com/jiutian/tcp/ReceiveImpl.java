package com.jiutian.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * ClassName:ReceiveImpl
 * Package:com.jiutian
 * Description:
 *
 * @Date: 2021/10/28 22:23
 * @Author: jiutian
 */
public class ReceiveImpl implements Runnable {
    private Socket socket;
    private String threadName;

    public ReceiveImpl(Socket socket, String threadName) {
        this.socket = socket;
        this.threadName = threadName;
    }

    @Override
    public void run() {
        while (true){
            try{
                InputStream inputStream = socket.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = bufferedReader.readLine();
                System.out.println(threadName+":"+line);
                bufferedReader.close();
                // System.out.println(Thread.currentThread().getName()+":"+line);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
