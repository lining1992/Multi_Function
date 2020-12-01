package com.baidu.iov.dueros.test.sokect;

import android.util.Log;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @author v_lining05
 * @date 2020/8/29
 */
public class TestClientSocket {

    public static void main(String[] args) {
        int i = 1;
        Socket socket = null;
        try {
            InetAddress inetAddress = InetAddress.getByName("127.0.0.1");
            socket = new Socket("127.0.0.1", 9999);
            System.out.println("socket.isConnected()=" + socket.isConnected());
            while (socket.isConnected()) {
                socket.getOutputStream().write(("I am Client " + i++).getBytes());
                Thread.sleep(2000);
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                byte[] bytes = new byte[1024];
                int read = dataInputStream.read(bytes);
                if (read > 0) {
                    System.out.println("debugli "
                            + new String(bytes, 0, read));
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
