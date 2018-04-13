package NettyLearning.ThreadLocalTest;

/**
 * Created by F on 2018/4/12.
 */
public class ThreadLocalTest1 {

    ThreadLocal<Long> longLocal = new ThreadLocal<>();
    ThreadLocal<String> stringLocal = new ThreadLocal<>();
    public void set(){
        longLocal.set(Thread.currentThread().getId());
        stringLocal.set(Thread.currentThread().getName());
    }

    public long getLong(){
        return longLocal.get();
    }
    public String getString(){
        return stringLocal.get();
    }
    public static void main(String[] args) throws InterruptedException{
        final ThreadLocalTest1 threadLocalTest1= new ThreadLocalTest1();

        threadLocalTest1.set();
        System.out.println(threadLocalTest1.getLong());
        System.out.println(threadLocalTest1.getString());

        Thread thread = new Thread(){
            public void run(){
                threadLocalTest1.set();
                System.out.println(threadLocalTest1.getLong());
                System.out.println(threadLocalTest1.getString());
            }
        };
        thread.start();
        thread.join();

        System.out.println(threadLocalTest1.getLong());
        System.out.println(threadLocalTest1.getString());

    }

}

