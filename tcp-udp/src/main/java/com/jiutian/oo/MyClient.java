package com.jiutian.oo;

import java.io.*;
import java.net.Socket;

/**
 * ClassName:MyClient
 * Package:com.jiutian.tcp
 * Description:
 *
 * @Date: 2021/10/28 18:00
 * @Author: jiutian
 */
public class MyClient {
    public static void main(String[] args) throws Exception {
        Socket socket=new Socket("127.0.0.1",6666);

        InputStream is = socket.getInputStream();
        OutputStream os = socket.getOutputStream();

        //使用缓冲流和转换流对is进行包装
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        //使用打印流对os进行包装
        PrintStream ps=new PrintStream(os);

        ps.println("您好吗？");

        String line = br.readLine();
        System.out.println("服务端响应的数据为："+line);

        socket.close();
    }
}
