package com.jiutian.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

/**
 * @author jiutian
 */
public class ReceiveSocket implements Runnable {
    private DatagramSocket socket;
    private String threadName;
    public ReceiveSocket(DatagramSocket socket, String threadName) {
        this.socket = socket;
        this.threadName = threadName;
    }

    @Override
    public void run() {
        Scanner sc = new Scanner(System.in);
        byte[] buf = new byte[1024];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        while (true) {
            try {
                socket.receive(packet);
                byte[] data = packet.getData();
                int len = packet.getLength();
                String s = new String(data, 0, len);
                //System.out.println(Thread.currentThread().getName() + ":" + s);
                System.out.println(threadName + ":" + s);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
