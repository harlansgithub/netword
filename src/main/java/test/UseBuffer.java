package test;

import java.nio.IntBuffer;

public class UseBuffer {
    static IntBuffer intBuffer = null;

    public static void main(String[] args) {
        intBuffer = IntBuffer.allocate(20);

        for (int i = 0; i < 5; i++){
            intBuffer.put(i);
        }
        intBuffer.mark();
//        intBuffer.flip();
//        intBuffer.clear();
        for (int i = 0;i<2;i++){
            int i1 = intBuffer.get();
            System.out.println("i="+i1);
        }
//        intBuffer.rewind();
//        for (int i = 0;i<6;i++){
//            int i1 = intBuffer.get();
//            System.out.println("i="+i1);
//        }
        System.out.println("---after allocate---");
        System.out.println("positon="+intBuffer.position());
        System.out.println("limit="+intBuffer.limit());
        System.out.println("capacity="+intBuffer.capacity());
        intBuffer.reset();
//        for (int i = 0;i<6;i++){
////            int i1 = intBuffer.get();
////            System.out.println("i="+i1);
////        }
        System.out.println("---after allocate---");
        System.out.println("positon="+intBuffer.position());
        System.out.println("limit="+intBuffer.limit());
        System.out.println("capacity="+intBuffer.capacity());

    }
}
