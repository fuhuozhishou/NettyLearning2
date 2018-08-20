package concurrent.chapter5;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by F on 2018/5/22.
 */
public class Memoizer<A, V> implements Computable<A, V> {
    private final Map<A, V> cache = new HashMap<>();
    private final Computable<A, V> c;

    public Memoizer(Computable c){
        this.c = c;
    }

    /**
     * 由于HashMap的线程不安全性，在执行compute的时候需要加锁来保证线程安全，
     * 糟糕的并发性，将导致多个线程等待一个线程计算完成，所花费的时间可能比无缓存的的操作更多
     * @param arg
     * @return
     * @throws InterruptedException
     */
    public synchronized V compute(A arg) throws InterruptedException{
        V result = cache.get(arg);
        if(result == null){
            result = c.compute(arg);
            cache.put(arg, result);
        }
        return result;
    }
}
