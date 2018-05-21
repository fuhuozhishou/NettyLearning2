package netty.rpcFramework.test;

import netty.rpcFramework.rpcClient.RpcClientProxyBuilder;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by F on 2018/5/14.
 */
public class RpcPerformanceTest
{
    public static JUnitTestInterface jUnitTestInterface;
    public final static int THREADS = 16;
    public final static int INVOKES = 10000;
    public final static int TIMEOUT = 300;

    public static void main(String[] args) throws Exception {
        jUnitTestInterface = RpcClientProxyBuilder.create(JUnitTestInterface.class)
                .timeout(0)
                .threads(4)
                .connect("127.0.0.1", 3721)
                .build();

        ExecutorService threadPool = Executors.newFixedThreadPool(THREADS);
        CountDownLatch countDownLatch = new CountDownLatch(THREADS * INVOKES);
        System.out.println("RpcPerformanceTest started.");
        long startTime = System.currentTimeMillis();
        for(int i=0; i<THREADS; i++) {
            threadPool.execute(new RpcPerformanceTestThread(jUnitTestInterface, countDownLatch));
        }

        if(countDownLatch.await(TIMEOUT, TimeUnit.SECONDS)) {
            long endTime = System.currentTimeMillis();
            double tps = THREADS * INVOKES / ((endTime - startTime) / 1000f);
            System.out.println("RpcPerformanceTest finished.");
            System.out.println("Result tps = " + tps);
        }
        else {
            System.out.println("RpcPerformanceTest failed.");
        }
    }

    private static class RpcPerformanceTestThread extends Thread {
        private JUnitTestInterface jUnitTestInterface;
        private CountDownLatch countDownLatch;

        public RpcPerformanceTestThread(JUnitTestInterface jUnitTestInterface,
                                        CountDownLatch countDownLatch) {
            this.jUnitTestInterface = jUnitTestInterface;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            for(int i=0; i<INVOKES; i++) {
                jUnitTestInterface.methodForPerformance();
                countDownLatch.countDown();
            }
        }
    }
}
