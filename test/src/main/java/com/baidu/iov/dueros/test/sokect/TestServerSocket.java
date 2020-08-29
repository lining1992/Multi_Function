package com.baidu.iov.dueros.test.sokect;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author v_lining05
 * @date 2020/8/29
 */
public class TestServerSocket {

    public static void main(String[] args) {
        int i = 1;
        try {
//            InetAddress allByName = InetAddress.getByName("127.0.0.1");
            ServerSocket serverSocket = new ServerSocket(9999);
            Socket socket = serverSocket.accept();
            System.out.println("serverSocket.isClosed()==" + serverSocket.isClosed()
                    + "=socket.isConnected()=" + socket.isConnected());
            while (!serverSocket.isClosed() && socket.isConnected()) {
                DataInputStream is = new DataInputStream(socket.getInputStream());
                byte[] bytes = new byte[1024];
                int len = is.read(bytes);
                System.out.println("debugli " + new String(bytes, 0, len));
                Thread.sleep(2000);
                socket.getOutputStream().write(("I am Server " + i++).getBytes());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
