package test;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileNIOCopyDemo {
    public static void main(String[] args) throws IOException {
        nioCopyResourceFile();
    }
    public static void nioCopyResourceFile() throws IOException {
        String sourcePath = "/Users/liudianfei/Downloads/入职填写-刘瑶.docx";
        String destPath = "/Users/liudianfei/Downloads/入职填写-刘瑶-1.docx";
        File srcFile = new File(sourcePath);
        File destFile = new File(destPath);
        if (!destFile.exists()){
            destFile.createNewFile();
        }
        long startTime = System.currentTimeMillis();
        FileInputStream fis = null;
        FileOutputStream fos = null;
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        fis = new FileInputStream(srcFile);
        fos = new FileOutputStream(destFile);
        inChannel = fis.getChannel();
        outChannel = fos.getChannel();
        int length = -1;
        ByteBuffer buf = ByteBuffer.allocate(1024);
        while ((length = inChannel.read(buf)) != -1){
            buf.flip();
            int outlength = 0;
            while ((outlength = outChannel.write(buf)) != 0){
                System.out.println("写入的字节数："+outlength);
            }
            buf.clear();
            outChannel.force(true);
        }

        outChannel.close();
        fos.close();
        inChannel.close();
        fis.close();
        long endtime = System.currentTimeMillis();
        System.out.println("base复制毫秒数："+(endtime - startTime));
    }
}
