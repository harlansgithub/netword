package io.nio;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class Client {
    private Selector selector;
    public void initclient() throws IOException {
        SocketChannel open = SocketChannel.open();
        open.configureBlocking(false);

        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        this.selector = Selector.open();
        socketChannel.connect(new InetSocketAddress("127.0.0.1",10001));
        socketChannel.register(selector,SelectionKey.OP_CONNECT);

    }
    public void connect() throws IOException {
        while (true){
            selector.select();
            Iterator<SelectionKey> it = this.selector.selectedKeys().iterator();
            while (it.hasNext()){
                SelectionKey key = it.next();
                it.remove();
                if (key.isConnectable()){
                    SocketChannel channel = (SocketChannel) key.channel();
                    if (channel.isConnectionPending()){
                        channel.finishConnect();
                    }
                    channel.configureBlocking(false);
                    ByteBuffer buffer = ByteBuffer.wrap("HelloServer".getBytes());
                    channel.write(buffer);
                    channel.register(this.selector, SelectionKey.OP_READ);
                }else if (key.isReadable()){
                    SocketChannel channel = (SocketChannel) key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(512);
                    buffer.clear();
                    int len = channel.read(buffer);
                    if (len != -1){

                        System.out.println("客户端收到信息：" + new String(buffer.array(),0,len));
                    }
                }
            }
        }
    }
    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.initclient();
        client.connect();
    }
}
