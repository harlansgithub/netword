package io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class Server {
    public static void main(String[] args) throws IOException {
        // NIO是面向管道的
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.socket().bind(new InetSocketAddress("127.0.0.1",10001));

        // 调用底层Selector
        Selector selector = Selector.open();
        // 将ServerSocketChannel注册到Selector上，并对请求连接的时间感兴趣，ServerSocketChannel只负责接受请求，所以一个线程就够了
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true){
            // select是阻塞的，是一个逆向的操作，当socket的文件描述符准备就绪时，通知应用层程序
            selector.select();
            // 有事件发生，获取全部发生的事件集合
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()){
                SelectionKey next = iterator.next();
                iterator.remove();
                // 如果当前事件是一个请求连接的事件，正是由于nio提供了判断事件类型的机制，体现了nio是非阻塞
                if (next.isAcceptable()){
                    ServerSocketChannel serverSocket = (ServerSocketChannel) next.channel();
                    // 获得请求连接请求的socket
                    SocketChannel socket = serverSocket.accept();
                    // 接受请求，并获取管道
                    // 设置为非阻塞，必须设置，否则会报错
                    socket.configureBlocking(false);
                    // 将当前的socketChannel注册到selector中，并关注读事件（这里的读是相对于从socket缓冲区中读取事件）
                    socket.register(next.selector(),SelectionKey.OP_READ);
                    System.out.println("客户端连接成功！");
                } else if (next.isReadable()){
                    SocketChannel socketChannel = (SocketChannel) next.channel();
                    // nio是面向buffer，bio是面向stream
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    byteBuffer.clear();
                    int read = socketChannel.read(byteBuffer);
                    String string = new String(byteBuffer.array(),0,read);
                    System.out.println("服务求收到消息：" + string);

                    byteBuffer.clear();
//                    byteBuffer.flip();
                    ByteBuffer buffer1 = ByteBuffer.wrap("hello client".getBytes());
                    socketChannel.write(buffer1);
                }
            }
        }
    }
}
