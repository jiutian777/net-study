package com.jiutian.net;

import com.jiutian.controller.UserController;

import java.io.*;
import java.net.Socket;

/**
 * @Date: 2022/3/20 21:31
 * @Author: jiutian
 * @Description:
 */
public class ServerRunnable implements Runnable {

    private final Socket socket;

    private final UserController userController = new UserController();

    public ServerRunnable(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        OutputStream os = null;
        PrintWriter pw = null;
        try {
            //来自客户端信息
            is = socket.getInputStream();
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);

            Long id = Long.parseLong(br.readLine());
            String pwd = br.readLine();
            String result = userController.login(id, pwd);
            socket.shutdownInput();

            //发送给客服端信息
            os = socket.getOutputStream();
            pw = new PrintWriter(os);
            pw.write(result);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        } finally {
            //关闭资源
            try {
                if (pw != null) {
                    pw.flush();
                    pw.close();
                }
                if (os != null) {
                    os.flush();
                    os.close();
                }
                if (br != null)
                    br.close();
                if (isr != null)
                    isr.close();
                if (is != null)
                    is.close();
                if (socket != null)
                    socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
