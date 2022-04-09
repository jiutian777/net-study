package com.jiutian.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

/**
 * @author jiutian
 */
public class SendSocket implements Runnable {
    private DatagramSocket socket;
    private int sendPort;
    private String address;

    public SendSocket(DatagramSocket socket, int sendPort,String address) {
        this.socket = socket;
        this.sendPort = sendPort;
        this.address = address;
    }

    @Override
    public void run() {
        Scanner sc = new Scanner(System.in);
        while (true) {
            try {
                System.out.println("请输入您要发送的数据：");
                String content = sc.nextLine();
                byte[] buf = content.getBytes();
                DatagramPacket packet = new DatagramPacket(buf, buf.length, InetAddress.getByName(address), sendPort);
                socket.send(packet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

