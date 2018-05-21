package JavaBasement;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;

import java.util.concurrent.TimeUnit;

/**
 * Created by F on 2018/4/19.
 */
public class VolatileTest {
    private static volatile boolean stop;
    /*public static void main(String[] args) throws InterruptedException{
        Thread workThread = new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while(!stop){
                    i++;
                    try{
                        TimeUnit.SECONDS.sleep(1);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
                System.out.println("Thread will stop!");
            }
        });
        workThread.start();
        TimeUnit.SECONDS.sleep(3);
        stop = true;
    }*/

    public static void main(String[] args){
        int loop = 3000;
        long startTime = System.currentTimeMillis();
        ByteBuf poolBuffer = null;
        for(int i = 0; i < loop; i++){
            poolBuffer = PooledByteBufAllocator.DEFAULT.directBuffer(1024);
            poolBuffer.writeBytes("Hello".getBytes());
            poolBuffer.release();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Pool Use Time : " + (endTime - startTime));

        long startTime2 = System.currentTimeMillis();
        ByteBuf buf = null;
        for(int i = 0; i < loop; i++){
            buf = Unpooled.directBuffer(1024);
            buf.writeBytes("Hello".getBytes());
            buf.release();
        }
        long endTime2 = System.currentTimeMillis();
        System.out.println("Unpool Use Time :" + (endTime2 - startTime2));


    }
}
