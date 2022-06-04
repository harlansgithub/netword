package io.bio;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(1000));
        int total = 0;
        while (true) {
            Socket accept = serverSocket.accept();
            System.out.println("收到的连接数是：" + (++total));
            ObjectInputStream inputStream = new ObjectInputStream(accept.getInputStream());
            String s = inputStream.readUTF();
            System.out.println(s);
        }
    }
}
