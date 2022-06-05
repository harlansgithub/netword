package io.nio;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;

public class Client {
    private Selector selector;
    private SocketChannel open;
    public void initclient() throws IOException {
        open = SocketChannel.open();
        open.configureBlocking(false);

        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        this.selector = Selector.open();
        socketChannel.connect(new InetSocketAddress("192.168.0.107",10002));
        socketChannel.register(selector,SelectionKey.OP_CONNECT);

    }
    public void handleEvents() throws IOException {
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


//                    open = channel;
//                    sendFile();
                }
            }
        }
    }
    public void sendFile() throws IOException {
        File fis = new File("/Users/liudianfei/Downloads/入职填写-刘瑶.docx");
        String destFileName = "入职填写-刘瑶.docx";
        FileChannel channel = new FileInputStream(fis).getChannel();
        ByteBuffer fileNameByteBuffer = Charset.defaultCharset().encode(destFileName);
        open.write(fileNameByteBuffer);
        ByteBuffer buffer = ByteBuffer.allocate(1000);
//        buffer.putLong(fis.length());
//        buffer.flip();
//        open.write(buffer);
//        buffer.clear();
        System.out.println("开始传输文件");
        int length = 0;
        long progress = 0;
        while ((length = channel.read(buffer)) > 0){
            buffer.flip();
            open.write(buffer);
            buffer.clear();
            progress += length;
            System.out.println("| "+(100 * progress / fis.length()) + "% |");
        }
//        if (length == -1){
//            channel.close();
//            open.shutdownOutput();
//            open.close();
//            System.out.println("文件传输成功");
//        }
    }
    public static void main(String[] args) throws IOException {
        Client client = new Client();
        client.initclient();
        client.handleEvents();
//        client.sendFile();
    }
}
